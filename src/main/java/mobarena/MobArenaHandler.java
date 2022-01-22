package mobarena;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class MobArenaHandler {

    private MobArena mod;

    /**
     * Primary constructor.
     * The field 'mod' is initalized, if the server is running MobArena.
     */
    public MobArenaHandler() {
        mod = (MobArena) FabricLoader.getInstance().getAllMods();
    }

        /*//////////////////////////////////////////////////////////////////
                 REGION/LOCATION METHODS
    //////////////////////////////////////////////////////////////////*/

    /**
     * Check if a Location is inside of any arena region.
     * @param l A location.
     * @return true, if the Location is inside of any arena region.
     */
    public boolean inRegion(ServerWorld w, BlockPos l) {
        for (Arena arena : mod.getArenaMaster().getArenas()) {
            if (arena.getRegion().contains(w, l)) {
                return true;
            }
        }

        return false;
    }

    public boolean inRegion(Arena arena, ServerWorld w, BlockPos l) {
        return (arena != null && arena.getRegion().contains(w, l));
    }

    public boolean inRegion(String arenaName, ServerWorld w, BlockPos l) {
        Arena arena = mod.getArenaMaster().getArenaWithName(arenaName);
        if (arena == null)
            throw new NullPointerException("There is no arena with that name");

        return arena.getRegion().contains(w, l);
    }

    public boolean inRunningRegion(ServerWorld w, BlockPos l) {
        return inRegion(w, l, false, true);
    }

    public boolean inEnabledRegion(ServerWorld w, BlockPos l) {
        return inRegion(w, l, true, false);
    }

    private boolean inRegion(ServerWorld w, BlockPos l, boolean enabled, boolean running) {
        // If the plugin doesn't exist, always return false.
        if (mod.getArenaMaster() == null) return false;

        // Return true if location is within just one arena's region.
        for (Arena arena : mod.getArenaMaster().getArenas()) {
            if (arena.getRegion().contains(w , l)) {
                if ((running && arena.isRunning()) || (enabled && arena.isEnabled())) {
                    return true;
                }
            }
        }

        return false;
    }



    /*//////////////////////////////////////////////////////////////////
                 PLAYER/MONSTER/PET METHODS
    //////////////////////////////////////////////////////////////////*/

    public boolean isPlayerInArena(ServerPlayerEntity player) {
        return (mod.getArenaMaster().getArenaWithPlayer(player) != null);
    }

    public boolean isMonsterInArena(LivingEntity entity) {
        return mod.getArenaMaster().getArenaWithMonster(entity) != null;
    }

    /*//////////////////////////////////////////////////////////////////
                 ARENA GETTERS
    //////////////////////////////////////////////////////////////////*/

    public Arena getArenaAtLocation(ServerWorld w, BlockPos l) {
        return mod.getArenaMaster().getArenaAtLocation(w, l);
    }

    public Arena getArenaWithPlayer(ServerPlayerEntity p) {
        return mod.getArenaMaster().getArenaWithPlayer(p);
    }

    public Arena getArenaWithMonster(LivingEntity monster) {
        return mod.getArenaMaster().getArenaWithMonster(monster);
    }

}
