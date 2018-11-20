package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.createTable;
import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.getExistingTables;
import static edu.colostate.cs.cs414.p3.bdeining.sql.HandlerUtils.getResultSetById;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.MACHINE_TABLE_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.MACHINE_TABLE_NAME;

import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.MachineHandler;
import edu.colostate.cs.cs414.p3.bdeining.impl.MachineImpl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
  name = "MachineHandlerImpl", //
  property = { //
    "service.exported.interfaces=*", //
  }
)
public class MachineHandlerImpl implements MachineHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandlerImpl.class);

  private DataSource dataSource;

  @Reference
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    init();
  }

  public void init() {
    LOGGER.trace("Initializing {}", CustomerHandlerImpl.class.getName());
    createTablesIfNonExistent();
  }

  private void createTablesIfNonExistent() {
    List<String> tables = getExistingTables(dataSource);
    LOGGER.trace("Existing tables : {}", tables);

    if (!tables.contains(MACHINE_TABLE_NAME)) {
      createTable(dataSource, MACHINE_TABLE_NAME, MACHINE_TABLE_DEF);
    }
  }

  @Override
  public boolean addMachine(Machine machine) throws SQLException {
    String id = machine.getId();
    String name = machine.getName();
    String picture = machine.getPicture();
    int quantity = machine.getQuantity();

    Machine existingCustomer = getMachineById(id);
    if (existingCustomer != null) {
      LOGGER.trace("Updating Customer : ID {}", id);

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding customer : {}", existingCustomer);

        PreparedStatement update =
            con.prepareStatement(
                "update " + MACHINE_TABLE_NAME + " SET name=?, picture=?, quantity=? WHERE id=?");

        update.setString(1, name);
        update.setString(2, picture);
        update.setInt(3, quantity);
        update.setString(4, id);
        update.execute();
        update.close();
      }

    } else {

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding machine : {}", machine);

        PreparedStatement insert =
            con.prepareStatement(
                "INSERT INTO "
                    + MACHINE_TABLE_NAME
                    + " (name, id, picture, quantity) VALUES (?,?,?,?)");
        insert.setString(1, name);
        insert.setString(2, id);
        insert.setString(3, picture);
        insert.setInt(4, quantity);
        insert.execute();
        insert.close();
      }
    }

    return true;
  }

  @Override
  public List<Machine> getMachines() throws SQLException {
    try (Connection con = dataSource.getConnection()) {

      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + MACHINE_TABLE_NAME);

      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet == null) {
        preparedStatement.close();
        return Collections.emptyList();
      }

      List<Machine> machineList = new ArrayList<>();
      while (resultSet.next()) {
        Machine machine = getMachine(resultSet);
        if (machine != null) {
          machineList.add(machine);
        }
      }
      preparedStatement.close();
      return machineList;
    }
  }

  @Override
  public boolean removeMachine(String id) throws SQLException {
    HandlerUtils.removeById(dataSource, id, MACHINE_TABLE_NAME);
    return true;
  }

  private Machine getMachine(ResultSet resultSet) {
    try {
      String id = resultSet.getString("id");
      String name = resultSet.getString("name");
      String picture = resultSet.getString("picture");
      int quantity = resultSet.getInt("quantity");

      Machine machine = new MachineImpl(id, name, picture, quantity);
      LOGGER.trace("Got machine {}", machine);
      return machine;
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  private Machine getMachineById(String id) throws SQLException {
    try (Connection con = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          con.prepareStatement("SELECT * FROM " + MACHINE_TABLE_NAME + " where id=?");

      ResultSet resultSet = getResultSetById(preparedStatement, id);
      if (resultSet == null) {
        return null;
      }

      while (resultSet.next()) {
        Machine machine = getMachine(resultSet);
        if (machine != null) {
          return machine;
        }
      }
      preparedStatement.close();
    }
    return null;
  }
}
