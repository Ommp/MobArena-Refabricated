package mobarena.database;

import mobarena.MobArena;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
    private PreparedStatement statement;
    private ResultSet result;

    private String query;
    private int paramIndex = 1;
    private boolean skippedNext = false;

    boolean success;

    public Query(String query) {
        this.query = query;
        try {
            statement = Database.conn.prepareStatement(query);
        } catch (SQLException e) {error();}
    }

    public Query set(Object ...items) {
        try {
            for (Object item : items) {
                statement.setObject(paramIndex, item);
                paramIndex++;
            }
        } catch (SQLException e) {error();}
        return this;
    }

    public String getString(String columnName) {
        try {
            return result.getString(columnName);
        } catch (SQLException e) {
            error();
        }
        return null;
    }

    public double getDouble(String columnName) {
        try {
            return result.getDouble(columnName);
        } catch (SQLException e) {error();}
        return 0;
    }


    public Query executeUpdate() {
        try {
            int affectedRows = statement.executeUpdate();
            success = affectedRows != 0;
        } catch (SQLException e) {error();}
        return this;
    }

    public Query executeQuery() {
        try {
            result = statement.executeQuery();
            success = result.next();
        } catch (SQLException e) {error();}
        return this;
    }

    public boolean next() {
        try {
            if (skippedNext) return result.next();
        } catch (SQLException e) {error();}
        skippedNext = true;
        return success;
    }

    private void error() {
        MobArena.LOGGER.error("Error executing database transaction {}", query);
    }
}