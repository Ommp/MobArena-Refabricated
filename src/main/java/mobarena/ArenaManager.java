package mobarena;

public class ArenaManager {

    public static void loadArena(String name) {
        MobArena.database.getArenaByName(name);
    }

}
