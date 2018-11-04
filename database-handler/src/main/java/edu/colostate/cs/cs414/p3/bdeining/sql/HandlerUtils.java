package edu.colostate.cs.cs414.p3.bdeining.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandlerImpl.class);

    public static void removeById(DataSource dataSource, String id, String tableName) throws SQLException {
        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement()) {
            LOGGER.trace("Removing from table {} : {}", tableName, id);
            stmt.execute(String.format("DELETE FROM %s WHERE ID = '%s';", tableName, id));
        }
    }

    public static void removeById(DataSource dataSource, String idFieldName, String id, String tableName) throws SQLException {
        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement()) {
            LOGGER.trace("Removing from table {} : {}", tableName, id);
            stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';", tableName, idFieldName, id));
        }
    }
}
