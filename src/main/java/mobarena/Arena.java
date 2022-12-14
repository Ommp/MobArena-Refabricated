package mobarena;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.Wave.WaveManager;
import mobarena.Wave.WaveType;
import mobarena.access.MobEntityAccess;
import mobarena.config.ArenaModel;
import mobarena.region.Region;
import mobarena.utils.MobUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Arena {

    public String name;
    private String dimensionName;
    private ServerWorld world;
    private boolean isRunning, isProtected;
    public int isEnabled;

    private final ArrayList<ServerPlayerEntity> arenaPlayers = new ArrayList<>();
    private final HashSet<ServerPlayerEntity> specPlayers= new HashSet<>();
    private final HashSet<ServerPlayerEntity> deadPlayers= new HashSet<>();
    private final HashSet<ServerPlayerEntity> readyLobbyPlayers = new HashSet<>();
    private final HashSet<ServerPlayerEntity> anyArenaPlayer = new HashSet<>();

    private final HashSet<ServerPlayerEntity> lobbyPlayers = new HashSet<>();

    private final HashMap<String, ArenaClass> playerClasses = new HashMap<>();

    Scoreboard scoreboard = new Scoreboard();

    private Warp arena, lobby, exit, spectator;

    private int minPlayers;
    private int maxPlayers;

    private Region arenaRegion;

    public Arena(String name) {
        this.name = name;
    }

    private final WaveManager waveManager = new WaveManager();
    public Spawner spawner;

    private int arenaStartCountdown;
    private int waveCountdown;

    private boolean arenaCountingDown = false;

    private boolean forceClass;

    private boolean isXPAllowed = false;

    public ArenaModel config = new ArenaModel();

    private final RewardManager rewardManager = new RewardManager();

    final ScheduledExecutorService waveService = Executors.newSingleThreadScheduledExecutor();
    ScheduledExecutorService entityService = Executors.newSingleThreadScheduledExecutor();

    public Arena(String name, int minPlayers, int maxPlayers, Warp lobby, Warp arena, Warp spectator, Warp exit, BlockPos p1, BlockPos p2, int isEnabled, String dimensionName, int arenaStartCountdown, int waveCountdown, boolean forceClass, boolean isXPAllowed, boolean isProtected) {
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.lobby = lobby;
        this.arena = arena;
        this.spectator = spectator;
        this.exit = exit;
        this.arenaRegion = new Region(p1, p2);
        this.isEnabled = isEnabled;
        this.dimensionName = dimensionName;
        this.arenaStartCountdown = arenaStartCountdown;
        this.waveCountdown = waveCountdown;
        this.forceClass = forceClass;
        this.isXPAllowed = isXPAllowed;
        this.isProtected = isProtected;
        this.world = setWorld();
        this.mobSpawnPoints = setMobSpawnPoints(name);
        this.spawner = new Spawner(name);
    }

    public Arena(String name, BlockPos p1, BlockPos p2, String dimensionName, boolean isProtected) {
        this.name = name;
        this.arenaRegion = new Region(p1, p2);
        this.dimensionName = dimensionName;
        this.world = setWorld();
        this.isProtected = isProtected;
    }
    public ServerWorld setWorld(){
        if (!(dimensionName == null) && !dimensionName.isEmpty()) {
            world = MobArena.serverinstance.getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(dimensionName)));
        } else {
            world = MobArena.serverinstance.getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft:overworld")));
        }
        return world;
    }

    public ArrayList<BlockPos> mobSpawnPoints = new ArrayList<>();

    public ArrayList<BlockPos> setMobSpawnPoints(String name) {
        ArrayList<BlockPos> pointsList;
        pointsList = MobArena.database.getMobSpawnPoints(name);

        //if mob spawn points table has no entries, set mob spawn points to be the same as the arena warp
        if (pointsList.isEmpty()) {
            pointsList.add(new BlockPos(arena.x, arena.y+1, arena.z));
            return pointsList;
        }
        return pointsList;
    }
    public void startArena() {
        if (lobbyPlayers.isEmpty()) {
            isRunning = true;
            rewardManager.setRewards(name);

            waveManager.addDefaultWaves();
            waveManager.addCustomWaves(config);
            var mobs = waveManager.prepareWaveReturnMobs(config, arenaPlayers);

            spawner.addEntitiesToSpawn(mobs, world);
            spawner.spawnMobs(world);
            spawner.modifyMobStats(waveManager.getWave().getType(), arenaPlayers.size());
            startEntityService();
        } else {
            arenaCountingDown = false;
        }
    }

    public void stopArena() {
        isRunning = false;
        spawner.clearMonsters();
        waveService.shutdownNow();
        entityService.shutdownNow();
        ArenaManager.loadInactiveArena(name);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void addLobbyPlayer(ServerPlayerEntity player) {
        lobbyPlayers.add(player);
        anyArenaPlayer.add(player);
    }

    public boolean hasMinPlayers() {
        return lobbyPlayers.size() + arenaPlayers.size() >= minPlayers;
    }

    public boolean hasLessThanMaxPlayers() {
        return lobbyPlayers.size() < maxPlayers;
    }

    public void countdownArenaStart() {
        if (!arenaCountingDown && (arenaPlayers.size() + lobbyPlayers.size() + readyLobbyPlayers.size() >= minPlayers)) {
            arenaCountingDown = true;
            for (ServerPlayerEntity p: anyArenaPlayer) {
                p.sendMessage(new TranslatableText("mobarena.countdown", arenaStartCountdown), true);
            }
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(this::startArena, arenaStartCountdown, TimeUnit.SECONDS);
        }
    }

    public void joinLobby(ServerPlayerEntity p) {
        if (hasLessThanMaxPlayers()) {
            PlayerManager.savePlayerInventory(p);
            PlayerManager.clearInventory(p);
            PlayerManager.setGameMode(p, GameMode.SURVIVAL);

            addLobbyPlayer(p);
            transportPlayer(p, WarpType.LOBBY);
            PlayerManager.restoreVitals(p);
            ArenaManager.addActivePlayer(p, name);
            scoreboard.addPlayer(p);
            p.sendMessage(new TranslatableText("mobarena.joinedarenalobby", name), true);
        } else {
            p.sendMessage(new TranslatableText("mobarena.maxplayersinarena"), false);
        }
    }

    public void leavePlayer(ServerPlayerEntity p) {
        lobbyPlayers.remove(p);
        PlayerManager.clearInventory(p);
        PlayerManager.restoreVitals(p);
        PlayerManager.restoreGameMode(p);
        transportPlayer(p, WarpType.EXIT);
        PlayerManager.retrieveItems(p);
        if (arenaPlayers.contains(p) || deadPlayers.contains(p)) {
            MobArena.database.updateScoreboard(name, p.getUuidAsString(), scoreboard.getWavesSurvived(p), Math.round(scoreboard.getPlayerDamage().get(p)), scoreboard.getPlayerKills().get(p));
            rewardManager.grantRewards(p, scoreboard.getWavesSurvived(p));
        }
        removePlayerFromArena(p);
        ArenaManager.removeActivePlayer(p);

        if (deadPlayers.size() == anyArenaPlayer.size()) {
            exitAllPlayers();
            stopArena();
        }
    }

    public boolean isPlayerInArena(ServerPlayerEntity player) {
        return anyArenaPlayer.contains(player);
    }

    public void addReadyLobbyPlayer(ServerPlayerEntity player) {
        readyLobbyPlayers.add(player);
        transportAllFromLobby();
    }

    public void addSpectatorPlayer(ServerPlayerEntity player) {
        specPlayers.add(player);
    }

    public void addDeadPlayer(ServerPlayerEntity player) {
            deadPlayers.add(player);
        if (deadPlayers.size() < anyArenaPlayer.size()) {
            arenaPlayers.remove(player);
            transportPlayer(player, WarpType.SPECTATOR);
            PlayerManager.restoreVitals(player);

        } else if (deadPlayers.size() == anyArenaPlayer.size()) {
            displayScoreboard();
            exitAllPlayers();
            stopArena();
        }
    }

    //"revive" a "dead" spectator player if another player successfully finishes the wave in the same arena
    public void reviveDead() {
        for (ServerPlayerEntity p: deadPlayers) {
            transportPlayer(p, WarpType.ARENA);
            deadPlayers.remove(p);
            arenaPlayers.add(p);
        }
    }

    //teleport all players to exit and restore various stats
    public void exitAllPlayers() {
        for (ServerPlayerEntity p : anyArenaPlayer) {
            PlayerManager.clearInventory(p);
            transportPlayer(p, WarpType.EXIT);
            PlayerManager.restoreGameMode(p);
            PlayerManager.restoreVitals(p);
            MobArena.database.updateScoreboard(name, p.getUuidAsString(), scoreboard.getWavesSurvived(p), Math.round(scoreboard.getPlayerDamage().get(p)), scoreboard.getPlayerKills().get(p));
        }
        for (ServerPlayerEntity p : anyArenaPlayer) {
            PlayerManager.retrieveItems(p);
            ArenaManager.removeActivePlayer(p);
            for (ServerPlayerEntity player : deadPlayers) {
                rewardManager.grantRewards(player, scoreboard.getWavesSurvived(p));
            }
        }



        for (ServerPlayerEntity p : specPlayers) {
            PlayerManager.clearInventory(p);
            transportPlayer(p, WarpType.EXIT);
            PlayerManager.restoreGameMode(p);
            PlayerManager.restoreVitals(p);

        }
        for (ServerPlayerEntity p : specPlayers) {

            PlayerManager.retrieveItems(p);
            ArenaManager.removeActivePlayer(p);
        }
                cleanUpPlayers();
    }

    public void displayScoreboard() {
        for (ServerPlayerEntity p: anyArenaPlayer) {
            for (ServerPlayerEntity p1: anyArenaPlayer) {
                p.sendMessage(new TranslatableText("mobarena.scoreboard", p1.getName()), false);
                p.sendMessage(new TranslatableText("mobarena.scoreboardKills", scoreboard.getPlayerKills().get(p1)), false);
                p.sendMessage(new TranslatableText("mobarena.scoreboardWavesSurvived", scoreboard.getWavesSurvived(p1)), false);
                p.sendMessage(new TranslatableText("mobarena.scoreboardTotalDamage", scoreboard.getPlayerDamage().get(p1)), false);
            }
        }
    }

    public void removePlayerFromArena(ServerPlayerEntity player) {
        anyArenaPlayer.remove(player);

        lobbyPlayers.remove(player);
        arenaPlayers.remove(player);
        readyLobbyPlayers.remove(player);
        specPlayers.remove(player);
        deadPlayers.remove(player);

        if (anyArenaPlayer.isEmpty()) {
            stopArena();
        }
    }

    public void cleanUpPlayers(){
        lobbyPlayers.clear();
        readyLobbyPlayers.clear();
        arenaPlayers.clear();
        specPlayers.clear();
        deadPlayers.clear();
        anyArenaPlayer.clear();
    }

    public void transportPlayer(ServerPlayerEntity player, WarpType warpType) {
        if (warpType.equals(WarpType.LOBBY)) {
            player.teleport(world, lobby.x,lobby.y,lobby.z, lobby.yaw, lobby.pitch);
        } else if (warpType.equals(WarpType.ARENA)) {
            player.teleport(world, arena.x, arena.y, arena.z, arena.yaw, arena.pitch);
        } else if (warpType.equals(WarpType.SPECTATOR)) {
            player.teleport(world, spectator.x, spectator.y, spectator.z, spectator.yaw, spectator.pitch);
        } else if (warpType.equals(WarpType.EXIT)) {
            player.teleport(world, exit.x, exit.y, exit.z, exit.yaw, exit.pitch);
        }

    }

    public void transportAllFromLobby() {
        if (hasMinPlayers() && readyLobbyPlayers.size() == lobbyPlayers.size()) {
            for (ServerPlayerEntity player : lobbyPlayers) {
                player.sendMessage(new TranslatableText("mobarena.allplayersready"), true);
                transportPlayer(player, WarpType.ARENA);
                arenaPlayers.add(player);
            }
            lobbyPlayers.clear();
            readyLobbyPlayers.clear();
            countdownArenaStart();
        }
    }

    public Vec3i getRandomSpawnPoint() {
        int index = (int)(Math.random() * mobSpawnPoints.size());
        return new Vec3i(mobSpawnPoints.get(index).getX(), mobSpawnPoints.get(index).getY(), mobSpawnPoints.get(index).getZ());
    }
    //get a random player's pos and choose the closest spawn point to the player
    public ServerPlayerEntity getRandomArenaPlayer() {
        int playerIndex = (int)(Math.random() * arenaPlayers.size());
        return arenaPlayers.get(playerIndex);
    }

    public Vec3i getSpawnPointNearPlayer(ServerPlayerEntity p) {
        BlockPos playerPos = p.getBlockPos();
        ArrayList<Double> distances = new ArrayList<>();
        for (Vec3i mobSpawnPoint : mobSpawnPoints) {
            distances.add(playerPos.getSquaredDistance(mobSpawnPoint));
        }
        int spawnPointIndex = distances.indexOf(Collections.min(distances));
        return mobSpawnPoints.get(spawnPointIndex);
    }

    public ServerPlayerEntity getClosestPlayer(Entity e) {

        ArrayList<Double> distancesToPlayers = new ArrayList<>();
        for (ServerPlayerEntity p : arenaPlayers) {
            distancesToPlayers.add(e.squaredDistanceTo(p));
        }
        int playerIndex = distancesToPlayers.indexOf(Collections.min(distancesToPlayers));
        return arenaPlayers.get(playerIndex);
    }

    public void countDeadMobs() {
        spawner.count();
        if (spawner.getDeadMonsters() == waveManager.getWave().getMobAmount()) {
            startNextWave();
        }
    }
    public void startNextWave() {
        for (var p: arenaPlayers) {
            scoreboard.incrementPlayerWave(p);
        }
        reviveDead();

        var mobs = waveManager.prepareWaveReturnMobs(config, arenaPlayers);

        Text waveText = Text.of("Wave: " + waveManager.getCurrentWave()).getWithStyle(Style.EMPTY.withFormatting(Formatting.GREEN)).get(0);
        for (var p: anyArenaPlayer) {
            var titleS2CPacket = new TitleS2CPacket(waveText);
            p.networkHandler.sendPacket(titleS2CPacket);
            p.networkHandler.sendPacket(new TitleFadeS2CPacket(10, 30, 5));
        }

        addReinforcementItems();

        waveService.schedule(() -> {
            spawner.clearMonsters();
            spawner.resetDeadMonsters();
            spawner.addEntitiesToSpawn(mobs, world);
            spawner.spawnMobs(world);
            spawner.modifyMobStats(waveManager.getWave().getType(), arenaPlayers.size());
            MobUtils.addEquipment(waveManager.getCurrentWave(), waveManager.getWave().getType(), spawner.getMonsters());
        }, waveCountdown, TimeUnit.SECONDS);

    }

    //save the player's class
    public void addPlayerClass(ServerPlayerEntity player, ArenaClass arenaClass) {
        this.playerClasses.put(player.getUuidAsString(), arenaClass);
        //clear player's inventory in case they switch to another class to avoid duplicating
        player.getInventory().clear();

        //add the items
        for (var arenaItem : arenaClass.getItems()) {
            var data = arenaItem.getData();
            ItemStack itemStack;
            try {
                itemStack = ItemStack.fromNbt(StringNbtReader.parse(data));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
            var slot = arenaItem.getSlot();

            player.getInventory().insertStack(slot,itemStack);
        }
    }
    public void addReinforcementItems() {
        for (var reinforcement : config.getReinforcements()) {
            //check if the reinforcement wave is equal to the current wave OR if it's a recurrent wave and can be used
            if (reinforcement.getWave() == waveManager.getCurrentWave() || reinforcement.isRecurrentCanBeUsed(waveManager.getCurrentWave())) {
                for (var classToItem : reinforcement.getClassItems().entrySet()) {
                    for (var player : arenaPlayers) {
                        if (classToItem.getKey().equals("all") || classToItem.getKey().equals(playerClasses.get(player.getUuidAsString()).getName())) {
                            for (String item : classToItem.getValue()) {
                                try {
                                    var itemStack = ItemStack.fromNbt(StringNbtReader.parse(item));
                                    player.getInventory().offerOrDrop(itemStack);
                                } catch (CommandSyntaxException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void initConfig(ArenaModel model) {
        this.config = model;
    }

    public boolean forceClass() {
        return forceClass;
    }

    public boolean playerHasClass(String uuid) {
        return playerClasses.containsKey(uuid);
    }

    public boolean getLobbyPlayer(ServerPlayerEntity p) {
        return lobbyPlayers.contains(p);
    }

    public boolean isXPAllowed() {
        return isXPAllowed;
    }

    public void transportStrayPlayers() {
        for (ServerPlayerEntity p: arenaPlayers) {
            if (!arenaRegion.isInsideRegion(p.getBlockPos())) {
                transportPlayer(p, WarpType.ARENA);
            }
        }
    }

    public void transportStrayMobs() {
        for (MobEntity e: spawner.getMonsters()) {
            if (!arenaRegion.isInsideRegion(e.getBlockPos())) {
                var spawnPoint = getSpawnPointNearPlayer(getClosestPlayer(e));
                e.updatePosition(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
            }
        }
    }

    public void makeMobsRetarget() {
        for (MobEntity e: spawner.getMonsters()) {
            e.setTarget(getClosestPlayer(e));

        }
    }

    public void updateAbilityTracker() {
        if (waveManager.getWave().getType().equals(WaveType.BOSS)) {

            for(var mob: spawner.getMonsters()) {
                if (!mob.isDead()) {

                    var abilityTracker = ((MobEntityAccess) mob).getAbilityTracker();

                    abilityTracker.setAbilityLimit();
                    abilityTracker.incrementAbilityCount();

                    if (abilityTracker.limitReached()) {
                        var ability = abilityTracker.selectRandomAbililty();
                        ability.use(mob);
                    }
                }
            }
            }
    }

    public void startEntityService() {
        entityService.scheduleAtFixedRate(() -> {
            transportStrayPlayers();
            transportStrayMobs();
            makeMobsRetarget();
            removeForeignEntities();
            updateAbilityTracker();
        }, 0, 250, TimeUnit.MILLISECONDS);
    }

    public void removeForeignEntities() {
        var foreignEntities= world.getEntitiesByType(TypeFilter.instanceOf(MobEntity.class), arenaRegion.asBox(), entity -> !belongsToArena(entity));

        for (var entity: foreignEntities) {
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    public void despawnItemEntitites() {
        //TODO might want to find a better predicate than isLiving
        var items = world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), arenaRegion.asBox(), itemEntity -> !itemEntity.isLiving());
        for (var item: items) {
            item.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    public boolean belongsToArena(MobEntity entity) {
        return spawner.getMonsters().contains(entity);
    }

    public Region getArenaRegion() {
        return arenaRegion;
    }

    public boolean getIsProtected() {
        return isProtected;
    }

    public int getPlayerNumber() {
        return deadPlayers.size()+arenaPlayers.size()+lobbyPlayers.size()+ lobbyPlayers.size()+specPlayers.size();
    }

    public ServerWorld getWorld() {
        return world;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
