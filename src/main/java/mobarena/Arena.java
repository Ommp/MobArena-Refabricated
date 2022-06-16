package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.Set;

public class Arena {

    public String name;
    public String worldName;

    private boolean isRunning, isProtected, inEditMode;
    public boolean isEnabled;

//    public int maxPlayers, minPlayers;

    private Set<ServerPlayerEntity> arenaPlayers, lobbyPlayers, specPlayers, deadPlayers, readyLobbyPlayers;
    private Set<ServerPlayerEntity> anyArenaPlayer;

    public HashMap<String, ArenaClass> arenaClasses = new HashMap<>();

    public Warp arena, lobby, exit, spectator;
    public ArenaRegion arenaRegion;
    public LobbyRegion lobbyRegion;

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
    }

    public void addReadyLobbyPlayer(ServerPlayerEntity player) {
        lobbyPlayers.remove(player);
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

    public void cleanUpPlayers(){
        lobbyPlayers.clear();
        readyLobbyPlayers.clear();
        arenaPlayers.clear();
        specPlayers.clear();
        deadPlayers.clear();
    }

    public void transportPlayer(ServerPlayerEntity player, Warp warp) {
        player.teleport(warp.coordinates.getX(),warp.coordinates.getY(),warp.coordinates.getZ());
    }

    //used for create command
    public Arena(String name, ServerWorld world) {
        this.name = name;
        this.worldName = world.toString();
    }


    public Arena(String name, ServerWorld world, boolean isEnabled,
                 HashMap<String, ArenaClass> arenaClasses, Warp arena, Warp lobby, Warp exit, Warp spectator,
                 ArenaRegion arenaRegion, LobbyRegion lobbyRegion) {
        this.name = name;
        this.worldName = world.toString();
        this.isEnabled = isEnabled;
        this.arenaClasses = arenaClasses;
        this.arena = arena;
        this.lobby = lobby;
        this.exit = exit;
        this.spectator = spectator;
        this.arenaRegion = arenaRegion;
        this.lobbyRegion = lobbyRegion;
    }



}
