package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ArenaManager {

    public HashMap<String, Arena> arenas = new HashMap<>();
    public HashMap<ServerPlayerEntity, String> activePlayers = new HashMap<>();

    public void loadArena(String name) {
        if (!checkArenaExists(name)) {
            Arena arena = MobArena.database.getArenaByName(name);
            arenas.put(name, arena);
        }
    }

    public void reloadArena(String name) {
        arenas.remove(name);
        loadArena(name);
    }

    //used in case a player tries to join another arena while already being in one
    public boolean isPlayerActive(ServerPlayerEntity player) {
        if (activePlayers.containsKey(player)) {
            return true;
        } else {
            return false;
        }
    }

    public void addActivePlayer(ServerPlayerEntity player, String arenaName) {
        activePlayers.put(player, arenaName);
    }

    public void removeActivePlayer(ServerPlayerEntity player) {
        String name = activePlayers.get(player);
        activePlayers.remove(player);
        arenas.get(name).removePlayerFromArena(player);
    }

    //TODO add logic that checks if an arena contains all REQUIRED data to run
    public void loadAllArenas() {
        ArrayList<Arena> arenaArrayList = MobArena.database.getAllArenas();

        for (Arena arena : arenaArrayList) {
            arenas.put(arena.name, arena);
        }
    }

    public boolean checkArenaExists(String name) {
        if (arenas.containsKey(name)) {
            return true;
        }
        return false;
    }
}
