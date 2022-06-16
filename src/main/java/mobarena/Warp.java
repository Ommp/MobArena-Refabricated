package mobarena;

import net.minecraft.util.math.BlockPos;

public class Warp {

    public BlockPos coordinates;

    public float Pitch;
    public float Yaw;

    public Warp(BlockPos coordinates, float pitch, float yaw) {
        this.coordinates = coordinates;
        Pitch = pitch;
        Yaw = yaw;
    }
}
