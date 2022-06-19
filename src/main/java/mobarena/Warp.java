package mobarena;

public class Warp {

    public float Pitch;
    public float Yaw;

    public float x,y,z;

    public Warp(float x, float y, float z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        Pitch = pitch;
        Yaw = yaw;
    }
}
