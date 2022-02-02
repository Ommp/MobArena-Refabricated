package mobarena;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class ArenaMasterImpl implements ArenaMaster {

    private MobArena mod;
    private List<Arena> arenas;
    private Map<ServerPlayerEntity, Arena> arenaMap;

    private boolean enabled;

    public ArenaMasterImpl(MobArena mod) {
        this.mod = mod;

        this.arenas = new ArrayList<>();
        this.arenaMap = new HashMap<>();
    }

    @Override
    public MobArena getMod() {
        return mod;
    }

    @Override
    public List<Arena> getArenas() {
        return arenas;
    }

    @Override
    public void addPlayer(ServerPlayerEntity player, Arena arena) {
        arenaMap.put(player, arena);
    }

    @Override
    public Arena removePlayer(ServerPlayerEntity player) {
        return arenaMap.remove(player);
    }

    @Override
    public Arena getArenaWithName(Collection<Arena> arenas, String name) {
        return getArenaWithName(this.arenas, name);
    }

    @Override
    public Arena getArenaWithName(String name) {
        return getArenaWithName(this.arenas, name);
    }

    @Override
    public List<Arena> getArenasInWorld(ServerWorld world) {
        List<Arena> result = new ArrayList<>(arenas.size());
        for (Arena arena : arenas)
            if (arena.getWorld().equals(world))
                result.add(arena);
        return result;
    }

    @Override
    public List<ServerPlayerEntity> getAllPlayers() {
        List<ServerPlayerEntity> result = new ArrayList<>(arenas.size());
        for (Arena arena : arenas)
            result.addAll(arena.getAllPlayers());
        return result;
    }

    @Override
    public List<ServerPlayerEntity> getAllPlayersInArena(String name) {
        Arena arena = getArenaWithName(name);
        return (arena != null) ? new ArrayList<>(arena.getPlayersInArena()) : new ArrayList<>();
    }

    @Override
    public List<ServerPlayerEntity> getAllLivingPlayers() {
        List<ServerPlayerEntity> result = new ArrayList<>();
        for (Arena arena : arenas)
            result.addAll(arena.getPlayersInArena());
        return result;
    }

    @Override
    public List<ServerPlayerEntity> getLivingPlayersInArena(String arenaName) {
        Arena arena = getArenaWithName(arenaName);
        return (arena != null) ? new ArrayList<>(arena.getPlayersInArena()) : new ArrayList<>();
    }

    @Override
    public Arena getArenaWithPlayer(ServerPlayerEntity player) {
        return arenaMap.get(player);
    }

    @Override
    public Arena getArenaWithSpectator(ServerPlayerEntity player) {
        for (Arena arena : arenas) {
            if (arena.getSpectators().contains(player))
                return arena;
        }
        return null;
    }

    @Override
    public Arena getArenaAtLocation(ServerWorld w, BlockPos l) {
        for (Arena arena: arenas)
            if (arena.getRegion().contains(w,l))
                return arena;
            return null;
    }

    public Arena getArenaWithMonster(Entity e) {
        for (Arena arena : arenas)
            if (arena.getMonsterManager().getMonsters().contains(e))
                return arena;
        return null;
    }

//    public void initialize() {
//        loadSettings();
////        loadArenas();
//    }

    /**
     * Load the global settings.
     */
//    public void loadSettings() {
//        FileConfig section = mod.getConfig();
//    }
//
//    public void reloadConfig() {
//        mod.reload();
//    }
//
//    public void saveConfig() {
//        mod.saveConfig();
//    }


}
