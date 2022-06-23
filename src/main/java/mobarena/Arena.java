package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashSet;

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
    }

    public ServerWorld setWorld(){
        if (!(dimensionName == null) && !dimensionName.isEmpty()) {
            world = MobArena.serverinstance.getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(dimensionName)));
        } else {
            world = MobArena.serverinstance.getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft:overworld")));
        }
        return world;
    }
}
