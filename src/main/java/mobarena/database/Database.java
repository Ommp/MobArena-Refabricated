package mobarena.database;

import mobarena.Arena;
import mobarena.ArenaPoint;
import mobarena.Warp;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    public Connection con;
    public void connectToDB() {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:./config/mobarena/mobarena", "sa", "");

            //create the main sql tables if they don't exist
            String arenaTable = "CREATE TABLE IF NOT EXISTS arenas(" +
                    "name varchar, " +
                    "minPlayers int DEFAULT 1, " +
                    "maxPlayers int DEFAULT 5, " +
                    "lobby_x float," +
                    "lobby_y float," +
                    "lobby_z float," +
                    "lobby_yaw float," +
                    "lobby_pitch float," +
                    "arena_x float," +
                    "arena_y float," +
                    "arena_z float," +
                    "arena_yaw float," +
                    "arena_pitch float," +
                    "spec_x float," +
                    "spec_y float," +
                    "spec_z float," +
                    "spec_yaw float," +
                    "spec_pitch float," +
                    "exit_x float," +
                    "exit_y float," +
                    "exit_z float," +
                    "exit_yaw float," +
                    "exit_pitch float," +
                    "p1_x int," +
                    "p1_y int," +
                    "p1_z int," +
                    "p2_x int," +
                    "p2_y int," +
                    "p2_z int," +
                    "isEnabled int DEFAULT 1," +
                    "worldName varchar," +
                    "worldID varchar," +
                    "PRIMARY KEY (name))";
            String classesTable = "CREATE TABLE IF NOT EXISTS classes(" +
                    "classname varchar UNIQUE," +
                    "helmet varchar," +
                    "chestplate varchar," +
                    "leggings varchar," +
                    "boots varchar)";
            String scoreboardTable = "CREATE TABLE IF NOT EXISTS scoreboard(" +
                    "player varchar UNIQUE," +
                    "score int," +
                    "deaths int)";

            Statement statement = con.createStatement();
            String[] tables = new String[] {arenaTable, classesTable, scoreboardTable};
            for (String table : tables) {
                statement.execute(table);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addArena(String name, int minPlayers, int maxPlayers) {
        String sql = "INSERT OR IGNORE INTO arenas(name, minPlayers, maxPlayers) VALUES(?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, minPlayers);
            statement.setInt(3, maxPlayers);
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
    public void updateLobbyWarp(String name, float x, float y, float z) {
        String sql = "UPDATE arenas SET lobby_x=?, lobby_y=?, lobby_z=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setFloat(1, x);
            statement.setFloat(2, y);
            statement.setFloat(3, z);
            statement.setString(4, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateArenaWarp(String name, float x, float y, float z) {
        String sql = "UPDATE arenas SET arena_x=?, arena_y=?, arena_z=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setFloat(1, x);
            statement.setFloat(2, y);
            statement.setFloat(3, z);
            statement.setString(4, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateExitWarp(String name, float x, float y, float z) {
        String sql = "UPDATE arenas SET exit_x=?, exit_y=?, exit_z=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setFloat(1, x);
            statement.setFloat(2, y);
            statement.setFloat(3, z);
            statement.setString(4, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateSpecWarp(String name, float x, float y, float z) {
        String sql = "UPDATE arenas SET spec_x=?, spec_y=?, spec_z=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setFloat(1, x);
            statement.setFloat(2, y);
            statement.setFloat(3, z);
            statement.setString(4, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createClass(String className) {
        String sql = "INSERT OR IGNORE INTO classes VALUES(?, ?, ?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, className);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateArmor(String className, String helmet, String chestplate, String leggings, String boots) {
        String sql = "UPDATE classes SET helmet=?, chestplate=?, leggings=?, boots=? WHERE name=?";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, helmet);
            statement.setString(2, chestplate);
            statement.setString(3, leggings);
            statement.setString(4, boots);
            statement.setString(5, className);
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

            return new Arena(rs.getString("name"), rs.getInt("minPlayers"),rs.getInt("maxPlayers"),
                    new Warp(rs.getFloat("lobby_x"), rs.getFloat("lobby_y"),rs.getFloat("lobby_z"),rs.getFloat("lobby_yaw"), rs.getFloat("lobby_pitch")),
                    new Warp(rs.getFloat("arena_x"), rs.getFloat("arena_y"), rs.getFloat("arena_z"), rs.getFloat("arena_yaw"), rs.getFloat("arena_pitch")),
                    new Warp(rs.getFloat("spec_x"), rs.getFloat("spec_y"), rs.getFloat("spec_z"), rs.getFloat("spec_yaw"), rs.getFloat("spec_pitch")),
                    new Warp(rs.getFloat("exit_x"), rs.getFloat("exit_y"), rs.getFloat("exit_z"), rs.getFloat("exit_yaw"), rs.getFloat("exit_pitch")),
                    new ArenaPoint(rs.getInt("p1_x"), rs.getInt("p1_y"), rs.getInt("p1_z")),
                    new ArenaPoint(rs.getInt("p2_x"), rs.getInt("p2_y"), rs.getInt("p2_z")),
                    rs.getInt("isEnabled"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                        new Warp(rs.getFloat("lobby_x"), rs.getFloat("lobby_y"),rs.getFloat("lobby_z"),rs.getFloat("lobby_yaw"), rs.getFloat("lobby_pitch")),
                        new Warp(rs.getFloat("arena_x"), rs.getFloat("arena_y"), rs.getFloat("arena_z"), rs.getFloat("arena_yaw"), rs.getFloat("arena_pitch")),
                        new Warp(rs.getFloat("spec_x"), rs.getFloat("spec_y"), rs.getFloat("spec_z"), rs.getFloat("spec_yaw"), rs.getFloat("spec_pitch")),
                        new Warp(rs.getFloat("exit_x"), rs.getFloat("exit_y"), rs.getFloat("exit_z"), rs.getFloat("exit_yaw"), rs.getFloat("exit_pitch")),
                        new ArenaPoint(rs.getInt("p1_x"), rs.getInt("p1_y"), rs.getInt("p1_z")),
                        new ArenaPoint(rs.getInt("p2_x"), rs.getInt("p2_y"), rs.getInt("p2_z")),
                        rs.getInt("isEnabled"));
                arenas.add(arena);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return arenas;
    }
}