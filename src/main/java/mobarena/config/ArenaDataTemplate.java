package mobarena.config;

import java.util.ArrayList;

public class ArenaDataTemplate extends MobArenaConfig {
    public String world;
    public boolean enabled = true;
    public ArrayList<Integer> arenaWarp;
    public ArrayList<Integer> exitWarp;
    public ArrayList<Integer> specWarp;
    public ArrayList<Integer> lobbyWarp;

    public ArrayList<Float> arenaWarpYawPitch;
    public ArrayList<Float> lobbyWarpYawPitch;
    public ArrayList<Float> exitWarpYawPitch;
    public ArrayList<Float> specWarpYawPitch;

    public ArrayList<Integer> p1;
    public ArrayList<Integer> p2;
    public ArrayList<Integer> l1;
    public ArrayList<Integer> l2;

    public ArenaDataTemplate() {
    }
}
