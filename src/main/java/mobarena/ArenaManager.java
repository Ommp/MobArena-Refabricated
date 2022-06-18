package mobarena;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaManager {

    public HashMap<String, Arena> arenas = new HashMap<>();

    public void loadArena(String name) {
        Arena arena = MobArena.database.getArenaByName(name);
        arenas.put(name, arena);
    }

    //TODO add logic that checks if an arena contains all REQUIRED data to run
    public void loadAllArenas() {
        ArrayList<Arena> arenaArrayList = MobArena.database.getAllArenas();

        for (Arena arena : arenaArrayList) {
            arenas.put(arena.name, arena);
        }
    }
}
