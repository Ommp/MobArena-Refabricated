package mobarena;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class Arena {
    public String arenaName;
    public String world;
    public double x_1;
    public double y_1;
    public double z_1;
    public double x_2;
    public double y_2;
    public double z_2;

    public Arena(String arenaName, double x_1, double y_1, double z_1, double x_2, double y_2 , double z_2, String world) {
        this.arenaName = arenaName;
        this.x_1 = x_1;
        this.y_1 = y_1;
        this.z_1 = z_1;
        this.x_2 = x_2;
        this.y_2 = y_2;
        this.z_2 = z_2;
        this.world =world;
    }

}
