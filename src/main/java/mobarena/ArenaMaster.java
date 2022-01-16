package mobarena;



import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.*;

public class ArenaMaster {

    private List<Arena> arenas;
    private Map<ServerPlayerEntity, Arena> arenaMap;


    public ArenaMaster() {
        this.arenas = new ArrayList<>();
        this.arenaMap = new HashMap<>();
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public void addPlayer(ServerPlayerEntity player, Arena arena) {
        arenaMap.put(player, arena);
    }

    public Arena removePlayer(ServerPlayerEntity player) {
        return arenaMap.remove(player);
    }

    public Arena getArenaWithName(Collection<Arena> arenas, String name) {
        return getArenaWithName(this.arenas, name);
    }

    public Arena getArenaWithName(String name) {
        return getArenaWithName(this.arenas, name);
    }

    public List<Arena> getArenasInWorld(ServerWorld world) {
        List<Arena> result = new ArrayList<>(arenas.size());
        for (Arena arena : arenas)
            if (arena.getWorld().equals(world))
                result.add(arena);
        return result;
    }

    public List<ServerPlayerEntity> getAllPlayers() {
        List<ServerPlayerEntity> result = new ArrayList<>(arenas.size());
        for (Arena arena : arenas)
            result.addAll(arena.getAllPlayers());
        return result;
    }

    public List<ServerPlayerEntity> getAllPlayersInArena(String name) {
        Arena arena = getArenaWithName(name);
        return (arena != null) ? new ArrayList<>(arena.getPlayersInArena()) : new ArrayList<>();
    }

    public List<ServerPlayerEntity> getAllLivingPlayers() {
        List<ServerPlayerEntity> result = new ArrayList<>();
        for (Arena arena : arenas)
            result.addAll(arena.getPlayersInArena());
        return result;
    }

    public List<ServerPlayerEntity> getLivingPlayersInArena(String arenaName) {
        Arena arena = getArenaWithName(arenaName);
        return (arena != null) ? new ArrayList<>(arena.getPlayersInArena()) : new ArrayList<>();
    }

    public Arena getArenaWithPlayer(ServerPlayerEntity player) {
        return arenaMap.get(player);
    }

    public Arena getArenaWithSpectator(ServerPlayerEntity player) {
        for (Arena arena : arenas) {
            if (arena.getSpectators().contains(player))
                return arena;
        }
        return null;
    }

//    public void loadArenasInWorld(String worldName) {
////        Set<String> arenaNames = config.getConfigurationSection("arenas").getKeys(false);
//
//        List<Arena> arenas = new ArrayList<>();
//        for (String arenaName : arenaNames) {
//            Arena arena = getArenaWithName(arenaName);
//            if (arena != null) continue;
//
//            Arena loaded = loadArena(arenaName);
//            if (loaded != null) {
//                arenas.add(loaded);
//            }
//        }
//    }


    private Arena loadArena(String name) {

//        ServerWorld world;

//        Arena arena = new Arena(name, world);
        Arena arena = new Arena(name);
        return arena;
    }




}
