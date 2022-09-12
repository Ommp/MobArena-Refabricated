package mobarena;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
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
    private final HashSet<ServerPlayerEntity> anyArenaPlayer = new HashSet<ServerPlayerEntity>();

    private final HashSet<ServerPlayerEntity> lobbyPlayers = new HashSet<ServerPlayerEntity>();



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

    public Arena(String name, int minPlayers, int maxPlayers, Warp lobby, Warp arena, Warp spectator, Warp exit, ArenaPoint p1, ArenaPoint p2, int isEnabled, String dimensionName) {
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
        isRunning = true;

        wave.setWaveType(WaveType.DEFAULT);
        wave.startWave();
        spawner.prepareSpawner(wave.getMobsToSpawn(), wave.getWaveType());
        spawner.spawnMobs();
    }

    public void stopArena() {
        isRunning = false;
        spawner.clearMonsters();
        MobArena.arenaManager.clearArena(name);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void addLobbyPlayer(ServerPlayerEntity player) {
        lobbyPlayers.add(player);
        anyArenaPlayer.add(player);
    }

    public void joinLobby(ServerPlayerEntity player) {
        if(isInventoryEmpty(player)) {
            addLobbyPlayer(player);
            transportPlayer(player, "lobby");
            player.changeGameMode(GameMode.ADVENTURE);
            restoreVitals(player);
            MobArena.arenaManager.addActivePlayer(player, name);
            player.sendMessage(new TranslatableText("mobarena.joinedarenalobby", name), false);
        } else {
            player.sendMessage(new TranslatableText("mobarena.inventorynotempty"), false);
        }
    }

    public void restoreVitals(ServerPlayerEntity player) {
        player.setHealth(20);
        player.getHungerManager().setFoodLevel(20);
    }

    public void leavePlayer(ServerPlayerEntity player) {
        player.changeGameMode(GameMode.SURVIVAL);
        transportPlayer(player, "exit");
        removePlayerFromArena(player);
        MobArena.arenaManager.removeActivePlayer(player);
        restoreVitals(player);
    }

    public boolean isPlayerInArena(ServerPlayerEntity player) {
        if (anyArenaPlayer.contains(player)) {
            return true;
        }
        return false;
    }

    public void addReadyLobbyPlayer(ServerPlayerEntity player) {
        readyLobbyPlayers.add(player);
        if (readyLobbyPlayers.size() == lobbyPlayers.size()) {
            transportAllFromLobby();
        }
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
            restoreVitals(player);

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
            restoreVitals(p);
            transportPlayer(p, "exit");
            p.getInventory().clear();
            p.changeGameMode(GameMode.SURVIVAL);
            MobArena.arenaManager.removeActivePlayer(p);
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
            for (ServerPlayerEntity player : lobbyPlayers) {
                player.sendMessage(new TranslatableText("mobarena.allplayersready"), false);
                transportPlayer(player, "arena");
                arenaPlayers.add(player);
            }
            lobbyPlayers.clear();
            readyLobbyPlayers.clear();
            startArena();
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
        for (int i = 0; i < mobSpawnPoints.size(); i++) {
            distances.add(playerPos.getSquaredDistance(mobSpawnPoints.get(i)));
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
        reviveDead();
        wave.setRandomWaveType();
        wave.startWave();
        spawner.clearMonsters();
        spawner.prepareSpawner(wave.getMobsToSpawn(), wave.getWaveType());
        spawner.spawnMobs();
        spawner.resetDeadMonsters();
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

    //use this until you decide to put resources into storing player inventory on disk
    public boolean isInventoryEmpty(ServerPlayerEntity player) {
        return player.getInventory().isEmpty();
    }

    public void setCustomSpawnConfigValues(boolean usesCustomSpawns, ArrayList<String> monsters) {
        if (usesCustomSpawns) {
            spawner.setPotentialMobs(monsters);
        } else {
            spawner.addDefaultMobs();
        }
    }
}
