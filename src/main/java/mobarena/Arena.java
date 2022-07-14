package mobarena;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Arena {

    public String name;
    public String dimensionName;
    public ServerWorld world;
    private boolean isRunning, isProtected, inEditMode;
    public int isEnabled;

    private final HashSet<ServerPlayerEntity> arenaPlayers = new HashSet<>();
    private final HashSet<ServerPlayerEntity> specPlayers= new HashSet<>();
    private final HashSet<ServerPlayerEntity> deadPlayers= new HashSet<>();
    private final HashSet<ServerPlayerEntity> readyLobbyPlayers = new HashSet<>();
    private final HashSet<ServerPlayerEntity> anyArenaPlayer = new HashSet<ServerPlayerEntity>();

    private final HashSet<ServerPlayerEntity> lobbyPlayers = new HashSet<ServerPlayerEntity>();

    public Warp arena, lobby, exit, spectator;

    public int minPlayers;
    public int maxPlayers;

    ArenaPoint p1, p2;

    public Arena(String name) {
        this.name = name;
    }

    public void startArena() {
        isRunning = true;
        startWave();
    }

    public void stopArena() {
        isRunning = false;
        cleanUpPlayers();
    }

    public void addLobbyPlayer(ServerPlayerEntity player) {
        lobbyPlayers.add(player);
        anyArenaPlayer.add(player);
    }

    public boolean isPlayerInArena(ServerPlayerEntity player) {
        if (anyArenaPlayer.contains(player)) {
            return true;
        }
        return false;
    }

    public void addReadyLobbyPlayer(ServerPlayerEntity player) {
        readyLobbyPlayers.add(player);
    }

    public void addArenaPlayer(ServerPlayerEntity player) {
        readyLobbyPlayers.remove(player);
        arenaPlayers.add(player);
    }

    public void addSpectatorPlayer(ServerPlayerEntity player) {
        specPlayers.add(player);
    }

    public void addDeadPlayer(ServerPlayerEntity player) {
        deadPlayers.add(player);
    }

    public void removePlayerFromArena(ServerPlayerEntity player) {
        anyArenaPlayer.remove(player);

        lobbyPlayers.remove(player);
        arenaPlayers.remove(player);
        readyLobbyPlayers.remove(player);
        specPlayers.remove(player);
        deadPlayers.remove(player);
    }

    public void cleanUpPlayers(){
        lobbyPlayers.clear();
        readyLobbyPlayers.clear();
        arenaPlayers.clear();
        specPlayers.clear();
        deadPlayers.clear();
    }

    public void transportPlayer(ServerPlayerEntity player, String warp) {
        if (warp == "lobby") {
            player.teleport(world, lobby.x,lobby.y,lobby.z, lobby.Yaw, lobby.Pitch);
        } else if (warp == "arena") {
            player.teleport(world, arena.x, arena.y, arena.z, arena.Yaw, arena.Pitch);
        } else if (warp == "spec") {
            player.teleport(world, spectator.x, spectator.y, spectator.z, spectator.Yaw, spectator.Pitch);
        } else if (warp == "exit") {
            player.teleport(world, exit.x, exit.y, exit.z, exit.Yaw, exit.Pitch);
        }

    }

    public void transportAllFromLobby() {
        if (readyLobbyPlayers.size() == lobbyPlayers.size() && lobbyPlayers.size() != 0 ) {
            for (ServerPlayerEntity player : lobbyPlayers) {
                player.sendMessage(new TranslatableText("mobarena.allplayersready"), false);
                player.teleport(arena.x, arena.y,arena.z);
                arenaPlayers.add(player);
                startWave();
                startScheduler();
            }
            lobbyPlayers.clear();
            readyLobbyPlayers.clear();
        }
    }

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

    private int currentWave = 0;
    private int finalWave;
    private double waveMobs;

    private final Timer timer = new Timer();

    ArrayList<LivingEntity> mobs = new ArrayList<>();


    public void populateMobs() {
        mobs.clear();
        ZombieEntity zombie = EntityType.ZOMBIE.create(world);
        SpiderEntity spider = EntityType.SPIDER.create(world);
        PiglinBruteEntity piglinBrute = EntityType.PIGLIN_BRUTE.create(world);
        HuskEntity husk = EntityType.HUSK.create(world);
        mobs.add(piglinBrute);
        mobs.add(zombie);
        mobs.add(spider);
        mobs.add(husk);
    }
    public void spawnMob() {
        int randomEntity = new Random().nextInt(mobs.size());
        int randomSpawnPoint = new Random().nextInt(mobSpawnPoints.size());
        world.spawnEntity(mobs.get(randomEntity));
        mobs.get(randomEntity).teleport(mobSpawnPoints.get(randomSpawnPoint).getX(), mobSpawnPoints.get(randomSpawnPoint).getY(), mobSpawnPoints.get(randomSpawnPoint).getZ());
    }

    public void startWave() {
        populateMobs();
        currentWave++;
        waveMobs = Math.round(currentWave * 1.2 + 3 );

        for (int i = 0; i <= waveMobs; i++) {
            spawnMob();
        }
    }

    public void startScheduler() {
        Runnable helloRunnable = new Runnable() {
            public void run() {
                    startWave();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 10, TimeUnit.SECONDS);
    }
}
