package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Set;

public class Arena {

    public String name;
    public String worldName;

    private boolean isRunning, isProtected, inEditMode;
    public int isEnabled;

    private Set<ServerPlayerEntity> arenaPlayers, lobbyPlayers, specPlayers, deadPlayers, readyLobbyPlayers;
    private Set<ServerPlayerEntity> anyArenaPlayer;

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

    public Arena(String name, int minPlayers, int maxPlayers, Warp lobby, Warp arena, Warp spectator, Warp exit, ArenaPoint p1, ArenaPoint p2, int isEnabled) {
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
    }
}
