package mobarena.database;

public class Warp {
    public String arenaName;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public String world;

    public static Warp getMainWarp(String arenaName) {
        Query query = new Query("SELECT * FROM MainWarp WHERE arena = ?;")
                .set(arenaName)
                .executeQuery();

        if (!query.success) return null;
        return new Warp(arenaName, query.getDouble("x"), query.getDouble("y"), query.getDouble("z"),
                query.getFloat("yaw"), query.getFloat("pitch"), query.getString("world")
        );
    }

    public static Warp setMainWarp(String arenaName, double x, double y, double z, float yaw, float pitch, String world){
        Query query = new Query("INSERT INTO MainWarp VALUES (?, ?, ?, ?, ?, ?, ?);")
                .set(arenaName, x, y, z, yaw, pitch, world)
                .executeUpdate();
        if (!query.success) return null;
        return new Warp(arenaName, x, y, z, yaw, pitch, world);
    }

    public static Warp setLobbyWarp(String arenaName, double x, double y, double z, float yaw, float pitch, String world){
        Query query = new Query("MERGE INTO LobbyWarp KEY (arena) VALUES (?, ?, ?, ?, ?, ?, ?)")
                .set(arenaName, x, y, z, yaw, pitch, world)
                .executeUpdate();
        if (!query.success) return null;
        return new Warp(arenaName, x, y, z, yaw, pitch, world);
    }

    public static Warp setExitWarp(String arenaName, double x, double y, double z, float yaw, float pitch, String world){
        Query query = new Query("MERGE INTO ExitWarp KEY (arena) VALUES (?, ?, ?, ?, ?, ?, ?)")
                .set(arenaName, x, y, z, yaw, pitch, world)
                .executeUpdate();
        if (!query.success) return null;
        return new Warp(arenaName, x, y, z, yaw, pitch, world);
    }

    public Warp(String arena, double x, double y, double z, float yaw, float pitch, String world){
        this.arenaName = arena;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }
}
