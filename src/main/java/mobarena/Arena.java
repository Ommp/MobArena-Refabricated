package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Set;

public class Arena {

    private String name;
    public String worldName;
    public boolean isRunning, isProtected, inEditMode;
    public boolean isEnabled;

    public int maxPlayers, minPlayers;

    public Set<ServerPlayerEntity> arenaPlayers, lobbyPlayers, specPlayers, deadPlayers, readyLobbyPlayers;

    public Arena(String name) {
        this.name = name;
    }


}
