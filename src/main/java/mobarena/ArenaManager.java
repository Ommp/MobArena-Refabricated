package mobarena;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaManager {
    private HashMap<String, Arena> arenas = new HashMap<>();

    private static ArenaManager arenaManager = new ArenaManager();

    private ArenaManager() {
    }

    public static ArenaManager getInstance() {
        return arenaManager;
    }

    public HashMap<String, Arena> getArenas() {
        return arenas;
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public void setArenas(HashMap<String, Arena> arenas) {
        this.arenas = arenas;
    }

    public void addArenaToMap(String name) {
        Arena arena = new Arena(name);
        arenas.put(name, arena);
    }

}
