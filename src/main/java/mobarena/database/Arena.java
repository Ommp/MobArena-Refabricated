package mobarena.database;

import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class Arena {
    public String name;
    public double x_1;
    public double y_1;
    public double z_1;
    public double x_2;
    public double y_2;
    public double z_2;
    public String world;


    public static Arena get(String name) {
        Query query = new Query("SELECT * FROM Arena WHERE name = ?;")
                .set(name)
                .executeQuery();

        if (!query.success) return null;

        return new Arena(query.getString("name"), query.getDouble("x_1"), query.getDouble("y_1"), query.getDouble("z_1"), query.getDouble("x_2"), query.getDouble("y_2"), query.getDouble("x_2"), query.getString("world"));
    }

    public Arena(String name, double x_1, double y_1, double z_1, double x_2, double y_2, double z_2, String world) {
        this.name = name;
        this.x_1 = x_1;
        this.y_1 = y_1;
        this.z_1 = z_1;
        this.x_2 = x_2;
        this.y_2 = y_2;
        this.z_2 = z_2;
        this.world = world;
    }

    public static Arena add(String name, double x_1, double y_1, double z_1, double x_2, double y_2, double z_2, String world) {
        Query query = new Query("INSERT INTO Arena VALUES (?, ?, ?, ?, ?, ?, ?, ?);")
                .set(name, x_1, y_1, z_1, x_2, y_2, z_2, world)
                .executeUpdate();

        if (!query.success) {
            return null;
        }
        return new Arena(name, x_1, y_1, z_1, x_2, y_2, x_2, world);
    }

    public void deleteArena() {
        new Query("DELETE FROM Arena WHERE name = ?;")
                .set(name)
                .executeUpdate();
    }

    public static ArrayList<Arena> all() {
        Query query = new Query("SELECT * FROM Arena;")
                .executeQuery();

        ArrayList<Arena> arenas = new ArrayList<Arena>();
        if (!query.success) return arenas;

        while (query.next()) {
            arenas.add(new Arena(query.getString("name"), query.getDouble("x_1"), query.getDouble("y_1"), query.getDouble("z_1"), query.getDouble("x_2"), query.getDouble("y_2"), query.getDouble("x_2"), query.getString("world")));
        }
        return arenas;
    }

}
