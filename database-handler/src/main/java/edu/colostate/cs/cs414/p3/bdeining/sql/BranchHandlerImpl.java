package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.createTable;
import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.getExistingTables;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.BRANCH_TABLE_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.BRANCH_TABLE_NAME;

import edu.colostate.cs.cs414.p3.bdeining.api.handlers.BranchHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
  immediate = true, //
  name = "BranchHandlerImpl", //
  property = { //
    "service.exported.interfaces=*", //
  }
)
public class BranchHandlerImpl implements BranchHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandlerImpl.class);

  private DataSource dataSource;

  /**
   * Sets the data source for the class; this is a reference to the data source that is registered
   * as a service in OSGi
   *
   * @param dataSource the given data source
   */
  @Reference
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    init();
  }

  /** Called when the class is instantiated. */
  public void init() {
    LOGGER.trace("Initializing {}", CustomerHandlerImpl.class.getName());
    createTablesIfNonExistent();
  }

  /** Creates the tables that this handler uses if they have not been added in data store. */
  private void createTablesIfNonExistent() {
    List<String> tables = getExistingTables(dataSource);
    LOGGER.trace("Existing tables : {}", tables);

    if (!tables.contains(BRANCH_TABLE_NAME)) {
      createTable(dataSource, BRANCH_TABLE_NAME, BRANCH_TABLE_DEF);
    }
  }

  @Override
  public boolean addBranch(String branch) throws SQLException {
    try (Connection con = dataSource.getConnection()) {

      PreparedStatement insert =
          con.prepareStatement("INSERT INTO " + BRANCH_TABLE_NAME + " (name) VALUES (?)");

      insert.setString(1, branch);
      insert.execute();
      insert.close();
    }
    return true;
  }

  @Override
  public boolean removeBranch(String branch) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", BRANCH_TABLE_NAME, branch);
      stmt.execute(String.format("DELETE FROM %s WHERE name = '%s';", BRANCH_TABLE_NAME, branch));
    }
    return true;
  }

  @Override
  public List<String> getBranches() throws SQLException {
    try (Connection con = dataSource.getConnection()) {

      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + BRANCH_TABLE_NAME);

      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet == null) {
        preparedStatement.close();
        return Collections.emptyList();
      }

      List<String> branches = new ArrayList<>();
      while (resultSet.next()) {
        String branch = resultSet.getString("name");
        if (branch != null) {
          branches.add(branch);
        }
      }

      preparedStatement.close();
      return branches;
    }
  }
}
