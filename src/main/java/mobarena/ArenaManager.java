package mobarena;

import org.apache.logging.log4j.Level;
import java.util.HashMap;

public class ArenaManager {

    public HashMap<String, Arena> arenas = new HashMap<>();

    public void loadArena(String name) {
        Arena arena = MobArena.database.getArenaByName(name);
        arenas.put(name, arena);
    }
}
