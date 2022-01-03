package mobarena.database;

import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class Arena {
    public String name;
    public String world;

    //constructor for simplified initial arena creation
    public Arena(String name, String world) {
        this.name = name;
        this.world = world;
    }


    public static Arena get(String name) {
        Query query = new Query("SELECT * FROM Arena WHERE name = ?;")
                .set(name)
                .executeQuery();

        if (!query.success) return null;

        return new Arena(query.getString("name"), query.getString("world"));
    }

    public static Arena add(String name, String world) {
        Query query = new Query("INSERT INTO Arena VALUES (?, ? );")
                .set(name, world)
                .executeUpdate();

        if (!query.success) {
            return null;
        }
        return new Arena(name, world);
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
            arenas.add(new Arena(query.getString("name"), query.getString("world")));
        }
        return arenas;
    }

}
