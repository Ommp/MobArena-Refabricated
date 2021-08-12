package mobarena;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class Arena {
    private String arenaName;
    private RegistryKey<World> world;
    private Vec3d pos1;
    private Vec3d pos2;

    public Arena(String arenaName, Vec3d pos1, Vec3d pos2, RegistryKey<World> world) {
        this.arenaName = arenaName;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.world =world;

    }

}
