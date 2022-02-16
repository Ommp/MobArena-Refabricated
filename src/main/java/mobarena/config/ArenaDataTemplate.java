package mobarena.config;

import mobarena.ArenaRegion;
import mobarena.LobbyRegion;
import mobarena.Warp;

import java.util.ArrayList;

public class ArenaDataTemplate extends MobArenaConfig {
    public String name;
    public String world;
    public boolean enabled = true;
//    public int minPlayers;
//    public int maxPlayers;
    public Warp arena;
    public Warp lobby;
    public Warp exit;
    public Warp spectator;

    public ArenaRegion arenaRegion;
    public LobbyRegion lobbyRegion;

    public ArenaDataTemplate() {
    }
}
