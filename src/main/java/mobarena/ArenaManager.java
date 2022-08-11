package mobarena;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaManager {

    public HashMap<String, Arena> arenas = new HashMap<>();
    public HashMap<ServerPlayerEntity, String> activePlayers = new HashMap<>();

    private HashMap<String, ArenaClass> classes = new HashMap<>();
    private HashMap<String, String> mobToArena = new HashMap<>();
    public void loadArena(String name) {
        if (!checkArenaExists(name)) {
            Arena arena = MobArena.database.getArenaByName(name);
            arenas.put(name, arena);
        }
    }

    public void initClasses() {
        ArrayList<ArenaClass> allClasses = MobArena.database.getClasses();
        for (ArenaClass arenaClass: allClasses) {
            classes.put(arenaClass.getName(), arenaClass);
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

    public HashMap<ServerPlayerEntity, String> getActivePlayers() {
        return activePlayers;
    }

    public void clearArena(String arenaName) {
        arenas.remove(arenaName);
    }

    public String getArenaFromPlayer(ServerPlayerEntity player) {
        return activePlayers.get(player);
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

    public void tellArenaPlayerDeath(ServerPlayerEntity serverPlayerEntity){
        if (activePlayers.containsKey(serverPlayerEntity)) {
            arenas.get(activePlayers.get(serverPlayerEntity)).addDeadPlayer(serverPlayerEntity);
            }
        }

    public void handleSignEvent(String text1, String text2, ServerPlayerEntity player) {
        if (text1.equals("[arena]") && classes.containsKey(text2) && !activePlayers.isEmpty()) {
            arenas.get(activePlayers.get(player)).addPlayerClass(player.getName().getString(), classes.get(text2));
            }
        else if (text1.equals("[arena]") && text2.equals("ready")) {
            arenas.get(activePlayers.get(player)).addReadyLobbyPlayer(player);
            }
        }
    }