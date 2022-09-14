package mobarena;

import mobarena.config.ArenaClassConfig;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaManager {

    private static final ArrayList<String> arenaNames = new ArrayList<>();
    public static HashMap<String, Arena> arenas = new HashMap<>();
    private static final HashMap<ServerPlayerEntity, String> activePlayers = new HashMap<>();

    private static HashMap<String, ArenaClass> classes = new HashMap<>();
    private static final HashMap<String, String> mobToArena = new HashMap<>();

    public static void addArenaNames() {
        if (!arenaNames.isEmpty()) {
            arenaNames.clear();
        }
        arenaNames.addAll(MobArena.database.getAllArenaNames());
    }
    public static ArrayList<String> getArenaNames() {
        return arenaNames;
    }

    public static Arena getArena(String name) {
        return arenas.get(name);
    }

    public static void loadArena(String name) {
        if (!arenas.containsKey(name)) {
            Arena arena = MobArena.database.getArenaByName(name);

            if (MobArena.arenaConfig.configExists(name)) {
                arena.setCustomSpawnConfigValues(MobArena.arenaConfig.getArenaConfig(name).usesCustomSpawns(), MobArena.arenaConfig.getArenaConfig(name).getMonsters());
            } else {
                arena.setCustomSpawnConfigValues(false, null);
            }

            arenas.put(name, arena);
        }
    }

    public static void initClasses() {
            ArenaClassConfig config = new ArenaClassConfig();
            config.load();
            classes = config.getArenaClasses();
        }

    public static void reloadArena(String name) {
        arenas.remove(name);
        loadArena(name);
    }

    //used in case a player tries to join another arena while already being in one
    public static boolean isPlayerActive(ServerPlayerEntity player) {
        return activePlayers.containsKey(player);
    }

    public static void addActivePlayer(ServerPlayerEntity player, String arenaName) {
        activePlayers.put(player, arenaName);
    }

    public static void removeActivePlayer(ServerPlayerEntity player) {
        activePlayers.remove(player);
    }

    public static HashMap<ServerPlayerEntity, String> getActivePlayers() {
        return activePlayers;
    }

    public static void clearArena(String arenaName) {
        arenas.remove(arenaName);
    }

    public static String getArenaFromPlayer(ServerPlayerEntity player) {
        return activePlayers.get(player);
    }

    public static boolean checkArenaExists(String name) {
        return arenaNames.contains(name);
    }

    public static void connectMobToArena(String UUID, String arenaName) {
        mobToArena.put(UUID, arenaName);
    }

    public static void handleMobDeath(String UUID) {
        if (mobToArena.containsKey(UUID)) {
            arenas.get(mobToArena.get(UUID)).countDeadMobs();
        }
    }

    public static void handleSignEvent(String text1, String text2, ServerPlayerEntity player) {
        if (text1.equals("[arena]") && classes.containsKey(text2) && !activePlayers.isEmpty()) {
            arenas.get(activePlayers.get(player)).addPlayerClass(player, classes.get(text2));
            player.sendMessage(new TranslatableText("mobarena.selectedclass", text2), false);
            }
        else if (text1.equals("[arena]") && text2.equals("ready")) {
            arenas.get(activePlayers.get(player)).addReadyLobbyPlayer(player);
            }
        }

    public static HashMap<String, String> getMobToArena() {
        return mobToArena;
    }
}