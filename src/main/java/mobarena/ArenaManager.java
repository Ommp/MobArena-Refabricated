package mobarena;

import mobarena.config.ArenaClassConfig;
import net.minecraft.entity.damage.DamageSource;
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

    public static void loadActiveArena(String name) {
        MobArena.arenaConfig.load();
        if (arenas.get(name).getPlayerNumber() < 1) {
            Arena arena = MobArena.database.getArenaByName(name);
            arena.despawnItemEntitites();

            if (MobArena.arenaConfig.configExists(name)) {
                arena.initConfig(MobArena.arenaConfig.getArenaConfig(name));
            }

            arenas.put(name, arena);
        }
    }

    public static void loadAllArenas() {
        if (arenas.isEmpty()) {
            for (Arena arena : MobArena.database.getAllInactiveArenas()) {
                arenas.put(arena.name, arena);
            }
        }
    }

    public static void loadInactiveArena(String name) {
        arenas.put(name, MobArena.database.getInactiveArena(name));
    }

    public static void initClasses() {
            ArenaClassConfig config = new ArenaClassConfig();
            config.load();
            classes = config.getArenaClasses();
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

    public static Arena getArenaFromPlayer(ServerPlayerEntity player) {
        var arenaName = activePlayers.get(player);
        return arenas.get(arenaName);
    }

    public static boolean checkArenaExists(String name) {
        return arenaNames.contains(name);
    }

    public static void connectMobToArena(String UUID, String arenaName) {
        mobToArena.put(UUID, arenaName);
    }

    public static void handleMobDeath(String UUID, DamageSource source) {
        if (mobToArena.containsKey(UUID)) {
            arenas.get(mobToArena.get(UUID)).countDeadMobs();
            if (source.getSource() instanceof ServerPlayerEntity) {
                arenas.get(mobToArena.get(UUID)).scoreboard.increasePlayerKillCount((ServerPlayerEntity) source.getSource());
            }
        }
    }

    public static void handleCreeperExplosion(String UUID) {
        if (mobToArena.containsKey(UUID)) {
            arenas.get(mobToArena.get(UUID)).countDeadMobs();
        }
    }

    public static void handleSignEvent(String text1, String text2, ServerPlayerEntity player) {
        if (text1.equals("[arena]") && classes.containsKey(text2) && !activePlayers.isEmpty()) {
            arenas.get(activePlayers.get(player)).addPlayerClass(player, classes.get(text2));
            player.sendMessage(Text.translatable("mobarena.selectedclass", text2), false);
            }

        else if (text1.equals("[arena]") && text2.equals("ready")) {
            if (getArenaFromPlayer(player).forceClass()) {
                if (!getArenaFromPlayer(player).playerHasClass(player.getUuidAsString())) {
                    player.sendMessage(Text.translatable("mobarena.selectaclass"), false);
                }
                else {
                    getArenaFromPlayer(player).addReadyLobbyPlayer(player);
                }
            }
            if (!getArenaFromPlayer(player).forceClass()){
                getArenaFromPlayer(player).addReadyLobbyPlayer(player);
            }
         }
        }

    public static HashMap<String, String> getMobFromAnyArena() {
        return mobToArena;
    }

    public static String getArenaFromMob(String UUID) {
        return mobToArena.get(UUID);
    }
}