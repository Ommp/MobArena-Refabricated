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
                    """).executeUpdate();
            MobArena.LOGGER.info("[MOBARENA]: Successfully connected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            MobArena.LOGGER.info("Error connecting to and setting up database.");
        }
    }
    public static void disconnect() {
        try {
            conn.close();
            MobArena.LOGGER.info("Successfully disconnected from database");
        } catch (SQLException e) {
            MobArena.LOGGER.error("Error disconnecting from database");
        }
    }
}
