package mobarena;

import mobarena.config.ArenaDataTemplate;
import net.minecraft.block.SmokerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
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


    @Override
    public void initialize() {
        loadSettings();
        loadArenas();
        loadClasses();
    }
    @Override
    public void loadSettings() {
        try {
            MobArena.config.readGlobalConfigJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void loadArenas() {
        try {
            MobArena.config.readArenasJson();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> arenanames = MobArena.config.arenas.arenaList.keySet();
        //if no arenas are found, create default arena as example
        if (arenanames.isEmpty()) {
            createArenaNode("default");
        }

        arenas = new ArrayList<>();

        for (String arenaName : arenanames) {
            loadArena(arenaName);
        }

    }

    @Override
    public void loadClasses() {
        try {
            MobArena.config.readClassesJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveArenas(){
        try {
            MobArena.config.saveArenaJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //figure out how Arena createArenaNode would/should actually work
    @Override
    public void createArenaNode(String name) {
        MobArena.config.arenas.arenaList.put(name, new ArenaDataTemplate());
        saveArenas();
    }

    @Override
    public Arena createArenaNode(String name, ServerWorld world) {
        return createArenaNode(name, world, true);
    }

    private Arena createArenaNode(String name, ServerWorld world, boolean load) {
        if (MobArena.config.arenas.arenaList.containsKey(name)) {
            throw new IllegalArgumentException("Arena already exists!");
        }
        ArenaDataTemplate template = new ArenaDataTemplate();
        template.world = world.toString();
        MobArena.config.arenas.arenaList.put(name, template);
        saveArenas();

        return (load ? loadArena(name) : null);
    }



    private Arena loadArena(String name) {
        Arena arena = new ArenaImpl(mod, name);
        arenas.add(arena);
        MobArena.LOGGER.info("Loaded arena: " + name);
        return arena;
    }


}
