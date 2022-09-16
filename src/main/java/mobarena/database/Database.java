package mobarena.database;

import mobarena.Arena;
import mobarena.ArenaPoint;
import mobarena.Warp;
import net.minecraft.util.math.Vec3i;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    public Connection con;
    public void connectToDB() {
        try {
            con = DriverManager.getConnection("jdbc:h2:./config/mobarena/mobarena;DATABASE_TO_UPPER=false;", "sa", "");

            //create the main sql tables if they don't exist
            String arenaTable = "CREATE TABLE IF NOT EXISTS arenas(" +
                    "name varchar, " +
                    "minPlayers int DEFAULT 1, " +
                    "maxPlayers int DEFAULT 5, " +
                    "lobby_x double," +
                    "lobby_y double," +
                    "lobby_z double," +
                    "lobby_yaw REAL," +
                    "lobby_pitch REAL," +
                    "arena_x double," +
                    "arena_y double," +
                    "arena_z double," +
                    "arena_yaw REAL," +
                    "arena_pitch REAL," +
                    "spec_x double," +
                    "spec_y double," +
                    "spec_z double," +
                    "spec_yaw REAL," +
                    "spec_pitch REAL," +
                    "exit_x double," +
                    "exit_y double," +
                    "exit_z double," +
                    "exit_yaw REAL," +
                    "exit_pitch REAL," +
                    "p1_x int," +
                    "p1_y int," +
                    "p1_z int," +
                    "p2_x int," +
                    "p2_y int," +
                    "p2_z int," +
                    "isEnabled int DEFAULT 1," +
                    "dimension varchar," +
                    "PRIMARY KEY (name))";
            String scoreboardTable = "CREATE TABLE IF NOT EXISTS scoreboard(" +
                    "player varchar UNIQUE," +
                    "score int," +
                    "deaths int)";
            String mobSpawnpointsTable = "CREATE TABLE IF NOT EXISTS mobspawnpoints(" +
                    "arena varchar," +
                    "x double," +
                    "y double," +
                    "z double," +
                    "FOREIGN KEY(arena) REFERENCES arenas(name) ON DELETE CASCADE)";

            Statement statement = con.createStatement();
            String[] tables = new String[] {arenaTable, scoreboardTable, mobSpawnpointsTable};
            for (String table : tables) {
                statement.execute(table);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addArena(String name) {
        String sql = "INSERT INTO arenas(name) VALUES(?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteArena(String name) {
        String sql = "DELETE FROM arenas WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateP1(String name, int x, int y, int z) {
        String sql = "UPDATE arenas SET p1_x=?, p1_y=?, p1_z=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, x);
            statement.setInt(2, y);
            statement.setInt(3, z);
            statement.setString(4, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateP2(String name, int x, int y, int z) {
        String sql = "UPDATE arenas SET p2_x=?, p2_y=?, p2_z=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, x);
            statement.setInt(2, y);
            statement.setInt(3, z);
            statement.setString(4, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateLobbyWarp(String name, double x, double y, double z, float yaw, float pitch) {
        String sql = "UPDATE arenas SET lobby_x=?, lobby_y=?, lobby_z=?, lobby_yaw=?, lobby_pitch=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setDouble(1, x);
            statement.setDouble(2, y);
            statement.setDouble(3, z);
            statement.setFloat(4, yaw);
            statement.setFloat(5, pitch);
            statement.setString(6, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateArenaWarp(String name, double x, double y, double z, float yaw, float pitch) {
        String sql = "UPDATE arenas SET arena_x=?, arena_y=?, arena_z=?, arena_yaw=?, arena_pitch=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setDouble(1, x);
            statement.setDouble(2, y);
            statement.setDouble(3, z);
            statement.setFloat(4, yaw);
            statement.setFloat(5, pitch);
            statement.setString(6, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateExitWarp(String name, double x, double y, double z, float yaw, float pitch) {
        String sql = "UPDATE arenas SET exit_x=?, exit_y=?, exit_z=?, exit_yaw=?, exit_pitch=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setDouble(1, x);
            statement.setDouble(2, y);
            statement.setDouble(3, z);
            statement.setFloat(4, yaw);
            statement.setFloat(5, pitch);
            statement.setString(6, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateSpecWarp(String name, double x, double y, double z, float yaw, float pitch) {
        String sql = "UPDATE arenas SET spec_x=?, spec_y=?, spec_z=?, spec_yaw=?, spec_pitch=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setDouble(1, x);
            statement.setDouble(2, y);
            statement.setDouble(3, z);
            statement.setFloat(4, yaw);
            statement.setFloat(5, pitch);
            statement.setString(6, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateWorld(String dimension, String arenaName) {
        String sql = "UPDATE arenas SET dimension=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, dimension);
            statement.setString(2, arenaName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Arena getArenaByName(String name) {
        String sql = "SELECT * FROM arenas where name=? ";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();

            return new Arena(rs.getString("name"), rs.getInt("minPlayers"),rs.getInt("maxPlayers"),
                    new Warp(rs.getDouble("lobby_x"), rs.getDouble("lobby_y"),rs.getDouble("lobby_z"), rs.getFloat("lobby_yaw"), rs.getFloat("lobby_pitch")),
                    new Warp(rs.getDouble("arena_x"), rs.getDouble("arena_y"), rs.getDouble("arena_z"), rs.getFloat("arena_yaw"), rs.getFloat("arena_pitch")),
                    new Warp(rs.getDouble("spec_x"), rs.getDouble("spec_y"), rs.getDouble("spec_z"), rs.getFloat("spec_yaw"), rs.getFloat("spec_pitch")),
                    new Warp(rs.getDouble("exit_x"), rs.getDouble("exit_y"), rs.getDouble("exit_z"), rs.getFloat("exit_yaw"), rs.getFloat("exit_pitch")),
                    new ArenaPoint(rs.getInt("p1_x"), rs.getInt("p1_y"), rs.getInt("p1_z")),
                    new ArenaPoint(rs.getInt("p2_x"), rs.getInt("p2_y"), rs.getInt("p2_z")),
                    rs.getInt("isEnabled"),
                    rs.getString("dimension"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean arenaExists(String name) {
        String sql = "SELECT name FROM arenas where name=? ";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            return !rs.getString("name").isEmpty();
        } catch (SQLException e) {
            return false;
        }

    }
    public ArrayList<Arena> getAllArenas() {
        Arena arena;
        ArrayList<Arena> arenas = new ArrayList<Arena>();
        String sql = "SELECT * FROM arenas";
        PreparedStatement statement;
        try {
            statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                arena = new Arena(rs.getString("name"), rs.getInt("minPlayers"),rs.getInt("maxPlayers"),
                        new Warp(rs.getDouble("lobby_x"), rs.getDouble("lobby_y"),rs.getDouble("lobby_z"),rs.getFloat("lobby_yaw"), rs.getFloat("lobby_pitch")),
                        new Warp(rs.getDouble("arena_x"), rs.getDouble("arena_y"), rs.getDouble("arena_z"), rs.getFloat("arena_yaw"), rs.getFloat("arena_pitch")),
                        new Warp(rs.getDouble("spec_x"), rs.getDouble("spec_y"), rs.getDouble("spec_z"), rs.getFloat("spec_yaw"), rs.getFloat("spec_pitch")),
                        new Warp(rs.getDouble("exit_x"), rs.getDouble("exit_y"), rs.getDouble("exit_z"), rs.getFloat("exit_yaw"), rs.getFloat("exit_pitch")),
                        new ArenaPoint(rs.getInt("p1_x"), rs.getInt("p1_y"), rs.getInt("p1_z")),
                        new ArenaPoint(rs.getInt("p2_x"), rs.getInt("p2_y"), rs.getInt("p2_z")),
                        rs.getInt("isEnabled"),
                        rs.getString("dimension"));
                arenas.add(arena);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return arenas;
    }

    public ArrayList<String> getAllArenaNames() {
        ArrayList<String> names = new ArrayList<>();
        String sql = "SELECT name FROM arenas";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return names;
    }

    public void addMobSpawnPoint(String arena, double x, double y, double z) {
        String sql = "INSERT INTO mobspawnpoints(arena, x, y, z) VALUES (?,?,?,?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, arena);
            statement.setDouble(2, x);
            statement.setDouble(3, y);
            statement.setDouble(4, z);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Vec3i> getMobSpawnPoints(String arena) {
        String sql = "SELECT x,y,z FROM mobspawnpoints WHERE arena=? ";
        PreparedStatement statement;
        ArrayList<Vec3i> pointsList = new ArrayList<>();

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, arena);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                pointsList.add(new Vec3i(rs.getInt("x"), rs.getInt("y"), rs.getInt("z")));
            }

            return pointsList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}