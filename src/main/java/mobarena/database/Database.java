package mobarena.database;

import mobarena.MobArena;
import org.apache.logging.log4j.Level;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class Database {
    public Connection con;
    public void connectToDB() {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:./config/mobarena/mobarena", "sa", "");

            //create the main sql tables if they don't exist
            String arenaTable = "CREATE TABLE IF NOT EXISTS arenas(name varchar, " +
                    "minPlayers int DEFAULT 1, " +
                    "maxPlayers int, " +
                    "PRIMARY KEY (name))";
            String arenaWarpTable = "CREATE TABLE IF NOT EXISTS arenawarp(" +
                    "    name varchar UNIQUE," +
                    "    x float," +
                    "    y float," +
                    "    z float," +
                    "    FOREIGN KEY (name) REFERENCES arenas)";
            String specWarpTable = "CREATE TABLE IF NOT EXISTS specwarp(" +
                    "    name varchar UNIQUE," +
                    "    x float," +
                    "    y float," +
                    "    z float," +
                    "    FOREIGN KEY (name) REFERENCES arenas)";
            String lobbyWarpTable  = "CREATE TABLE IF NOT EXISTS lobbywarp(" +
                    "    name varchar UNIQUE," +
                    "    x float," +
                    "    y float," +
                    "    z float," +
                    "    FOREIGN KEY (name) REFERENCES arenas)";
            String exitWarpTable = "CREATE TABLE IF NOT EXISTS exitwarp(" +
                    "    name varchar UNIQUE," +
                    "    x float," +
                    "    y float," +
                    "    z float," +
                    "    FOREIGN KEY (name) REFERENCES arenas)";
            String p1Table = "CREATE TABLE IF NOT EXISTS p1(" +
                    "    name varchar UNIQUE," +
                    "    x int," +
                    "    y int," +
                    "    z int," +
                    "    FOREIGN KEY (name) REFERENCES arenas)";
            String p2Table = "CREATE TABLE IF NOT EXISTS p2(" +
                    "    name varchar UNIQUE," +
                    "    x int," +
                    "    y int," +
                    "    z int," +
                    "    FOREIGN KEY (name) REFERENCES arenas)";
            String classesTable = "CREATE TABLE IF NOT EXISTS classes(" +
                    "classname varchar UNIQUE," +
                    "helmet varchar," +
                    "chestplate varchar," +
                    "leggings varchar," +
                    "boots varchar)";

            Statement statement = con.createStatement();
            String[] tables = new String[] {arenaTable, specWarpTable, lobbyWarpTable, arenaWarpTable, exitWarpTable, p1Table, p2Table, classesTable};
            for (String table : tables) {
                statement.execute(table);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addArena(String name, int minPlayers, int maxPlayers) {
        String sql = "INSERT OR REPLACE INTO arenas VALUES(?, ?, ?)";
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
        String sql = "INSERT OR REPLACE INTO p1 VALUES(?, ?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, z);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateP2(String name, int x, int y, int z) {
        String sql = "INSERT OR REPLACE INTO p2 VALUES(?, ?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, x);
            statement.setInt(3, y);
            statement.setInt(4, z);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateLobbyWarp(String name, float x, float y, float z) {
        String sql = "INSERT OR REPLACE INTO lobbywarp VALUES(?, ?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.setFloat(2, x);
            statement.setFloat(3, y);
            statement.setFloat(4, z);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateArenaWarp(String name, float x, float y, float z) {
        String sql = "INSERT OR REPLACE INTO arenawarp VALUES(?, ?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.setFloat(2, x);
            statement.setFloat(3, y);
            statement.setFloat(4, z);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateExitWarp(String name, float x, float y, float z) {
        String sql = "INSERT OR REPLACE INTO exitwarp VALUES(?, ?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.setFloat(2, x);
            statement.setFloat(3, y);
            statement.setFloat(4, z);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateSpecWarp(String name, float x, float y, float z) {
        String sql = "INSERT OR REPLACE INTO specwarp VALUES(?, ?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            statement.setFloat(2, x);
            statement.setFloat(3, y);
            statement.setFloat(4, z);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateArmor(String className, String helmet, String chestplate, String leggings, String boots) {
        String sql = "INSERT OR REPLACE INTO classes VALUES(?, ?, ?, ?, ?)";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, className);
            statement.setString(2, helmet);
            statement.setString(3, chestplate);
            statement.setString(4, leggings);
            statement.setString(5, boots);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> getArenaByName(String name) {
        String sql = "SELECT * FROM arenas where name=? ";
        PreparedStatement statement;

        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            return Arrays.asList(rs.getString(1), rs.getInt(2), rs.getInt(3));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}