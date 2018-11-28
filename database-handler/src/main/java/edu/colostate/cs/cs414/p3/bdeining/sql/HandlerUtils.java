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

  /**
   * Removes an entry from a given table by id.
   *
   * @param dataSource the given data source
   * @param id the given id for a row in a table
   * @param tableName the table name
   * @throws SQLException when a database error occurs
   */
  public static void removeById(DataSource dataSource, String id, String tableName)
      throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE ID = '%s';", tableName, id));
    }
  }

  /**
   * Removed an entry from a given table by a given id. This overloaded function allows the caller
   * to specify what the id column is in the data store.
   *
   * @param dataSource the given data source
   * @param idFieldName the given column name for the id
   * @param id the given id for a row in the table
   * @param tableName the table name
   * @throws SQLException when a database error occurs
   */
  public static void removeById(
      DataSource dataSource, String idFieldName, String id, String tableName) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';", tableName, idFieldName, id));
    }
  }

  /**
   * Creates a table in the data source
   *
   * @param dataSource the given data source
   * @param tableName the table name to create
   * @param tableDefinition the table definition
   */
  public static void createTable(DataSource dataSource, String tableName, String tableDefinition) {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Creating table : {}", tableDefinition);
      stmt.execute("create table " + tableName + " " + tableDefinition);
    } catch (SQLException e) {
      LOGGER.error("Unable to create table {}", tableName, e);
    }
  }

  /**
   * Gets the existing tables from the data source
   *
   * @param dataSource the given data source
   * @return a list of table names
   */
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

  /**
   * Executes a {@link PreparedStatement} with a given id and returns the result set.
   *
   * @param preparedStatement the given statement
   * @param id the given id
   * @return a result set from the query
   * @throws SQLException when a database error occurs
   */
  public static ResultSet getResultSetById(PreparedStatement preparedStatement, String id)
      throws SQLException {
    preparedStatement.setString(1, id);
    return preparedStatement.executeQuery();
  }
}
