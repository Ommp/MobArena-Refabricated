package mobarena;

public class Warp {

    public float pitch;
    public float yaw;

    public double x,y,z;

    public Warp(double x, double y, double z,  float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
