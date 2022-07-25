package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaManager {

    public HashMap<String, Arena> arenas = new HashMap<>();
    public HashMap<ServerPlayerEntity, String> activePlayers = new HashMap<>();

    private HashMap<String, String> mobToArena = new HashMap<>();
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

    public void removeActivePlayer(ServerPlayerEntity player, String arenaName) {
        activePlayers.remove(player);
        arenas.get(arenaName).removePlayerFromArena(player);
        if (arenas.get(arenaName).getAnyArenaPlayerSize() == 0) {
            clearArena(arenaName);
        }
    }

    public void clearArena(String arenaName) {
        arenas.remove(arenaName);
    }

    public String getArenaFromPlayer(ServerPlayerEntity player) {
        return activePlayers.get(player);
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

    public void connectMobToArena(String UUID, String arenaName) {
        mobToArena.put(UUID, arenaName);
    }

    public void tellArenaMobDeath(String UUID) {
        if (mobToArena.containsKey(UUID)) {
            String name = mobToArena.get(UUID);
            arenas.get(mobToArena.get(UUID)).countDeadMobs();
        }
    }
}
