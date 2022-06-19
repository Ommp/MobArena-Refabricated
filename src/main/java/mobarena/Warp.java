package mobarena;

public class Warp {

    public float Pitch;
    public float Yaw;

    public double x,y,z;

    public Warp(double x, double y, double z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        Pitch = pitch;
        Yaw = yaw;
    }
}
