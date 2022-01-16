package mobarena;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Location {
    public BlockPos blockPos;
    public float yaw;
    public float pitch;

    public Location(BlockPos blockPos, float yaw, float pitch) {
        this.blockPos = blockPos;
        this.yaw = yaw;
        this.pitch = pitch;
    }




}
