package mobarena.database;

import mobarena.MobArena;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection conn;

    public static void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:./config/mobarena/mobarena", "sa", "");
            new Query("""
                                    CREATE TABLE IF NOT EXISTS Arena (
                                        name VARCHAR(255) PRIMARY KEY,
                                        x_1 REAL,
                                        y_1 REAL,
                                        z_1 REAL,
                                        x_2 REAL,
                                        y_2 REAL,
                                        z_2 REAL,
                                        world VARCHAR(255)
                                    );
                                    CREATE TABLE IF NOT EXISTS LobbyWarp (
                                    arena VARCHAR(255),
                                    x REAL,
                                    y REAL,
                                    z REAL,
                                    yaw REAL,
                                    pitch REAL,
                                    world VARCHAR(255),
                                    FOREIGN KEY(arena) REFERENCES Arena(name) ON DELETE CASCADE
                                    );
                                    CREATE TABLE IF NOT EXISTS ArenaWarp (
                                    arena VARCHAR(255),
                                    x REAL,
                                    y REAL,
                                    z REAL,
                                    yaw REAL,
                                    pitch REAL,
                                    world VARCHAR(255),
                                    FOREIGN KEY(arena) REFERENCES Arena(name) ON DELETE CASCADE
                                    );
                                    CREATE TABLE IF NOT EXISTS ExitWarp (
                                    arena VARCHAR(255),
                                    x REAL,
                                    y REAL,
                                    z REAL,
                                    yaw REAL,
                                    pitch REAL,
                                    world VARCHAR(255),
                                    FOREIGN KEY(arena) REFERENCES Arena(name) ON DELETE CASCADE
                                    );
                                    
                    """).executeUpdate();
            MobArena.LOGGER.info("[MOBARENA]: Successfully connected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            MobArena.LOGGER.info("[MOBARENA]:Error connecting to and setting up database.");
        }
    }
    public static void disconnect() {
        try {
            conn.close();
            MobArena.LOGGER.info("[MOBARENA]:Successfully disconnected from database");
        } catch (SQLException e) {
            MobArena.LOGGER.error("[MOBARENA]:Error disconnecting from database");
        }
    }
}
