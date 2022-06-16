package mobarena;

import java.io.IOException;
import java.util.HashMap;

public class ArenaLoader {

   private HashMap<String, Arena> arenas = new HashMap<>();

   public Arena getArena(String name) {
      return arenas.get(name);
   }

   public HashMap<String, Arena> getArenas() {
      return arenas;
   }

   public void loadAllArenas() {
      arenas = MobArena.config.arenas;
   }

   public void loadArena(Arena arena) {
      arenas.put(arena.name, arena);
   }

   public void saveArenas() throws IOException {
      MobArena.config.saveArenaJson();
   }

}
