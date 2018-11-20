package edu.colostate.cs.cs414.p3.bdeining.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO : Prepared statements
public class HandlerUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandlerImpl.class);

  public static void removeById(DataSource dataSource, String id, String tableName)
      throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE ID = '%s';", tableName, id));
    }
  }

  public static void removeById(
      DataSource dataSource, String idFieldName, String id, String tableName) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';", tableName, idFieldName, id));
    }
  }

  public static void createTable(DataSource dataSource, String tableName, String tableDefinition) {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Creating table : {}", tableDefinition);
      stmt.execute("create table " + tableName + " " + tableDefinition);
    } catch (SQLException e) {
      LOGGER.error("Unable to create table {}", tableName, e);
    }
  }

  public static List<String> getExistingTables(DataSource dataSource) {
    List<String> existingTables = new ArrayList<>();
    try (Connection con = dataSource.getConnection()) {
      DatabaseMetaData meta = con.getMetaData();

      ResultSet res = meta.getTables(null, null, "%", new String[] {"TABLE"});

      while (res.next()) {
        existingTables.add(res.getString("TABLE_NAME"));
      }

    } catch (SQLException e) {
      LOGGER.error("Unable to fetch exiting tables.", e);
    }
    return existingTables;
  }

  public static ResultSet getResultSetById(PreparedStatement preparedStatement, String id)
      throws SQLException {
    preparedStatement.setString(1, id);
    ResultSet resultSet = preparedStatement.executeQuery();

    return resultSet;
  }
}
