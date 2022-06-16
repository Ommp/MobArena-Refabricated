package mobarena.config;

import mobarena.ArenaRegion;
import mobarena.LobbyRegion;
import mobarena.Warp;

public class ArenaDataTemplate extends MobArenaConfig {
    public String name;
    public String world;
    public boolean enabled = true;

    public Warp arena;
    public Warp lobby;
    public Warp exit;
    public Warp spectator;

    public ArenaRegion arenaRegion;
    public LobbyRegion lobbyRegion;

    public ArenaDataTemplate() {
    }
}
