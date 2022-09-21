package mobarena;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
    private boolean isRunning, isProtected, inEditMode;
    public int isEnabled;

    private final ArrayList<ServerPlayerEntity> arenaPlayers = new ArrayList<>();
    private final HashSet<ServerPlayerEntity> specPlayers= new HashSet<>();
    private final HashSet<ServerPlayerEntity> deadPlayers= new HashSet<>();
    private final HashSet<ServerPlayerEntity> readyLobbyPlayers = new HashSet<>();
    private final HashSet<ServerPlayerEntity> anyArenaPlayer = new HashSet<>();

    private final HashSet<ServerPlayerEntity> lobbyPlayers = new HashSet<>();



    private final HashMap<String, ArenaClass> playerClasses = new HashMap<>();

    private Warp arena, lobby, exit, spectator;

    private int minPlayers;
    private int maxPlayers;

    private ArenaPoint p1, p2;

    public Arena(String name) {
        this.name = name;
    }

    private Wave wave = new Wave();
    public Spawner spawner;

    private int arenaStartCountdown;
    private boolean arenaCountingDown = false;

    private boolean forceClass;

    private final RewardManager rewardManager = new RewardManager();

    public Arena(String name, int minPlayers, int maxPlayers, Warp lobby, Warp arena, Warp spectator, Warp exit, ArenaPoint p1, ArenaPoint p2, int isEnabled, String dimensionName, int arenaStartCountdown, boolean forceClass) {
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.lobby = lobby;
        this.arena = arena;
        this.spectator = spectator;
        this.exit = exit;
        this.p1 = p1;
        this.p2 = p2;
        this.isEnabled = isEnabled;
        this.dimensionName = dimensionName;
        this.arenaStartCountdown = arenaStartCountdown;
        this.forceClass = forceClass;
        this.world = setWorld();
        this.mobSpawnPoints = setMobSpawnPoints();
        this.spawner = new Spawner(name, world);
    }

    public int getAnyArenaPlayerSize() {
        return anyArenaPlayer.size();
    }

    public ServerWorld setWorld(){
        if (!(dimensionName == null) && !dimensionName.isEmpty()) {
            world = MobArena.serverinstance.getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(dimensionName)));
        } else {
            world = MobArena.serverinstance.getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft:overworld")));
        }
        return world;
    }

    public ArrayList<Vec3i> mobSpawnPoints = new ArrayList<>();

    public ArrayList<Vec3i> setMobSpawnPoints() {
        ArrayList<Vec3i> pointsList;
        pointsList = MobArena.database.getMobSpawnPoints(name);

        //if mob spawn points table has no entries, set mob spawn points to be the same as the arena warp
        if (pointsList.isEmpty()) {
            pointsList.add(new Vec3i(arena.x, arena.y, arena.z));
            return pointsList;
        }
        return pointsList;
    }
    public void startArena() {
        if (lobbyPlayers.isEmpty()) {
            isRunning = true;
            rewardManager.setRewards(name);
            for (ServerPlayerEntity p : arenaPlayers) {
                rewardManager.addPlayer(p);
            }


            wave.setWaveType(WaveType.DEFAULT);
            wave.startWave();
            spawner.addEntitiesToSpawn(wave.getMobsToSpawn(), wave.getWaveType());
            spawner.spawnMobs();
        } else {
            arenaCountingDown = false;
            countdownArenaStart();
        }
    }

    public void stopArena() {
        isRunning = false;
        spawner.clearMonsters();
        ArenaManager.clearArena(name);
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
        if (!arenaCountingDown && (arenaPlayers.size() + lobbyPlayers.size() + readyLobbyPlayers.size() > 0)) {
            arenaCountingDown = true;
            for (ServerPlayerEntity p: anyArenaPlayer) {
                p.sendMessage(new TranslatableText("mobarena.countdown", arenaStartCountdown), false);
            }
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(this::startArena, arenaStartCountdown, TimeUnit.SECONDS);
        }
    }

    public void joinLobby(ServerPlayerEntity p) {
        if (hasLessThanMaxPlayers()) {
            PlayerManager.savePlayerInventory(p);
            PlayerManager.clearInventory(p);
            PlayerManager.setGameMode(p, GameMode.ADVENTURE);

            addLobbyPlayer(p);
            transportPlayer(p, "lobby");
            PlayerManager.restoreVitals(p);
            ArenaManager.addActivePlayer(p, name);
            p.sendMessage(new TranslatableText("mobarena.joinedarenalobby", name), true);
        } else {
            p.sendMessage(new TranslatableText("mobarena.maxplayersinarena"), false);
        }
    }

    public void leavePlayer(ServerPlayerEntity p) {
        PlayerManager.restoreVitals(p);
        PlayerManager.clearInventory(p);
        PlayerManager.retrieveItems(p);
        if (arenaPlayers.contains(p) || deadPlayers.contains(p)) {
            rewardManager.grantRewards(p);
        }
        PlayerManager.restoreGameMode(p);
        transportPlayer(p, "exit");
        removePlayerFromArena(p);
        ArenaManager.removeActivePlayer(p);


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
            addSpectatorPlayer(player);
            arenaPlayers.remove(player);
            transportPlayer(player, "spec");
            PlayerManager.restoreVitals(player);

        } else if (deadPlayers.size() == anyArenaPlayer.size()) {
            exitAllPlayers();
            stopArena();
        }
    }

    //"revive" a "dead" spectator player if another player successfully finishes the wave in the same arena
    public void reviveDead() {
        for (ServerPlayerEntity p: deadPlayers) {
            transportPlayer(p, "arena");
            deadPlayers.remove(p);
            specPlayers.remove(p);
            arenaPlayers.add(p);
        }
    }

    //teleport all players to exit and restore various stats
    public void exitAllPlayers() {
        for (ServerPlayerEntity p : anyArenaPlayer) {


            PlayerManager.clearInventory(p);
            PlayerManager.retrieveItems(p);
            PlayerManager.restoreVitals(p);
            PlayerManager.restoreGameMode(p);

            for (ServerPlayerEntity player: deadPlayers) {
                rewardManager.grantRewards(player);
            }

            transportPlayer(p, "exit");
            ArenaManager.removeActivePlayer(p);

        }
        cleanUpPlayers();
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

    public void transportPlayer(ServerPlayerEntity player, String warp) {
        if (warp == "lobby") {
            player.teleport(world, lobby.x,lobby.y,lobby.z, lobby.yaw, lobby.pitch);
        } else if (warp == "arena") {
            player.teleport(world, arena.x, arena.y, arena.z, arena.yaw, arena.pitch);
        } else if (warp == "spec") {
            player.teleport(world, spectator.x, spectator.y, spectator.z, spectator.yaw, spectator.pitch);
        } else if (warp == "exit") {
            player.teleport(world, exit.x, exit.y, exit.z, exit.yaw, exit.pitch);
        }

    }

    public void transportAllFromLobby() {
        if (hasMinPlayers() && readyLobbyPlayers.size() == lobbyPlayers.size()) {
            for (ServerPlayerEntity player : lobbyPlayers) {
                player.sendMessage(new TranslatableText("mobarena.allplayersready"), true);
                transportPlayer(player, "arena");
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
    public Vec3i getSpawnPointNearPlayer() {
        int playerIndex = (int)(Math.random() * arenaPlayers.size());
        BlockPos playerPos = arenaPlayers.get(playerIndex).getBlockPos();
        ArrayList<Double> distances = new ArrayList<>();
        for (Vec3i mobSpawnPoint : mobSpawnPoints) {
            distances.add(playerPos.getSquaredDistance(mobSpawnPoint));
        }
        int spawnPointIndex = distances.indexOf(Collections.min(distances));
        return mobSpawnPoints.get(spawnPointIndex);
    }

    public void countDeadMobs() {
        spawner.count();
        if (spawner.getDeadMonsters() == wave.getMobsToSpawn()) {
            startNextWave();
        }
    }
    public void startNextWave() {
        for (ServerPlayerEntity p: arenaPlayers) {
            rewardManager.incrementPlayerWave(p);
        }
        reviveDead();
        wave.setRandomWaveType();
        wave.startWave();
        Text waveText = Text.of("Wave: " + wave.getCurrentWave()).getWithStyle(Style.EMPTY.withFormatting(Formatting.GREEN)).get(0);

        for (ServerPlayerEntity p: anyArenaPlayer) {
            var titleS2CPacket = new TitleS2CPacket(waveText);
            p.networkHandler.sendPacket(titleS2CPacket);
            p.networkHandler.sendPacket(new TitleFadeS2CPacket(10, 30, 10));
        }

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                spawner.clearMonsters();
                spawner.addEntitiesToSpawn(wave.getMobsToSpawn(), wave.getWaveType());
                spawner.spawnMobs();
                spawner.resetDeadMonsters();
            }
        }, 4, TimeUnit.SECONDS);
    }

    //save the player's class
    public void addPlayerClass(ServerPlayerEntity player, ArenaClass arenaClass) {
        this.playerClasses.put(String.valueOf(player.getName()), arenaClass);
        //clear player's inventory in case they switch to another class to avoid duplicating
        player.getInventory().clear();

        //add the items
        for (int i = 0; i < arenaClass.getItems().size(); i++) {
            var data = arenaClass.getItem(i).getData();
            ItemStack itemStack;
            try {
                itemStack = ItemStack.fromNbt(StringNbtReader.parse(data));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
            var slot = arenaClass.getItems().get(i).getSlot();

            player.getInventory().insertStack(slot,itemStack);
        }
    }

    public void setCustomSpawnConfigValues(boolean usesCustomSpawns, ArrayList<String> monsters) {
        if (usesCustomSpawns) {
            spawner.setPotentialMobs(monsters);
        } else {
            spawner.addDefaultMobs();
        }
    }
}
