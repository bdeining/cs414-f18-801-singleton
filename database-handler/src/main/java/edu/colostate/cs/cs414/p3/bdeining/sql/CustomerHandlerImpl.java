package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.CUSTOMER_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TABLES;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TABLES_DEF;

import edu.colostate.cs.cs414.p3.bdeining.api.Activity;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.handlers.CustomerHandler;
import edu.colostate.cs.cs414.p3.bdeining.impl.CustomerImpl;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
  immediate = true, //
  name = "CustomerHandlerImpl", //
  property = { //
    "service.exported.interfaces=*", //
  }
)

// TODO: Prepared Statements
public class CustomerHandlerImpl implements CustomerHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHandlerImpl.class);

  private DataSource dataSource;

  public void init() {
    LOGGER.trace("Initializing {}", CustomerHandlerImpl.class.getName());
    createTablesIfNonExistent();
  }

  private void createTablesIfNonExistent() {
    List<String> tables = getExistingTables();
    LOGGER.trace("Existing tables : {}", tables);

    for (int i = 0; i < TABLES.size(); i++) {
      String tableName = TABLES.get(i);
      String tableDef = TABLES_DEF.get(i);
      if (!tables.contains(tableName)) {
        createTable(tableName, tableDef);
      }
    }
  }

  private List<String> getExistingTables() {
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

  private void createTable(String tableName, String tableDefinition) {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Creating table : {}", tableDefinition);
      stmt.execute("create table " + tableName + " " + tableDefinition);
    } catch (SQLException e) {
      LOGGER.error("Unable to create table {}", tableName, e);
    }
  }

  @Reference
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    init();
  }

  @Override
  public boolean addCustomer(Customer customer) throws SQLException {
    String address = customer.getAddress();
    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    String phone = customer.getPhone();
    String healthInsuranceProvider = customer.getHealthInsuranceProvider();
    String email = customer.getEmail();
    String id = customer.getId();
    String activity = customer.getActivity().toString();
    List<String> routines = customer.getWorkoutRoutineIds();

    Customer existingCustomer = getCustomerById(id);
    if (existingCustomer != null) {
      LOGGER.trace("Updating Customer : ID {}", id);

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding customer : {}", customer);

        PreparedStatement update =
            con.prepareStatement(
                "update "
                    + CUSTOMER_TABLE_NAME
                    + " SET first_name=?, last_name=?, address=?, phone=?, email=?, health_insurance_provider=?, activity=? WHERE id=?");

        update.setString(1, firstName);
        update.setString(2, lastName);
        update.setString(3, address);
        update.setString(4, phone);
        update.setString(5, email);
        update.setString(6, healthInsuranceProvider);
        update.setString(7, activity);
        update.setString(8, id);
        update.execute();
        update.close();
      }

      HandlerUtils.removeById(dataSource, "customerId", id, CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME);

      for (String routineId : routines) {

        try (Connection con = dataSource.getConnection()) {
          LOGGER.trace("Adding customer : {}", customer);
          PreparedStatement insert =
              con.prepareStatement(
                  "INSERT INTO "
                      + CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME
                      + " (workoutRoutineId, customerId) VALUES (?,?)");
          insert.setString(1, routineId);
          insert.setString(2, id);
          insert.execute();
          insert.close();
        }
      }

    } else {

      try (Connection con = dataSource.getConnection()) {
        LOGGER.trace("Adding customer : {}", customer);

        PreparedStatement insert =
            con.prepareStatement(
                "INSERT INTO "
                    + CUSTOMER_TABLE_NAME
                    + " (first_name, last_name, address, phone, email, id, health_insurance_provider, activity) VALUES (?,?,?,?,?,?,?,?)");
        insert.setString(1, firstName);
        insert.setString(2, lastName);
        insert.setString(3, address);
        insert.setString(4, phone);
        insert.setString(5, email);
        insert.setString(6, id);
        insert.setString(7, healthInsuranceProvider);
        insert.setString(8, activity);
        insert.execute();
        insert.close();
      }

      for (String routineId : routines) {
        try (Connection con = dataSource.getConnection()) {
          LOGGER.trace("Adding customer : {}", customer);
          PreparedStatement insert =
              con.prepareStatement(
                  "INSERT INTO "
                      + CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME
                      + " (workoutRoutineId, customerId) VALUES (?,?)");
          insert.setString(1, routineId);
          insert.setString(2, id);
          insert.execute();
          insert.close();
        }
      }
    }
    return true;
  }

  private Customer getCustomerById(String id) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(
              String.format("SELECT * FROM %s where id='%s'", CUSTOMER_TABLE_NAME, id));

      if (resultSet == null) {
        return null;
      }

      while (resultSet.next()) {
        Customer customer = getCustomer(resultSet);
        if (customer != null) {
          return customer;
        }
      }
      return null;
    }
  }

  @Override
  public boolean removeCustomer(String id) throws SQLException {
    HandlerUtils.removeById(dataSource, id, CUSTOMER_TABLE_NAME);
    HandlerUtils.removeById(dataSource, "customerId", id, CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME);
    return true;
  }

  @Override
  public List<Customer> getCustomers() throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(String.format("SELECT * FROM %s;", CUSTOMER_TABLE_NAME));

      List<Customer> customers = new ArrayList<>();
      while (resultSet.next()) {
        Customer customer = getCustomer(resultSet);
        if (customer != null) {
          customers.add(customer);
        }
      }
      return customers;
    }
  }

  private Customer getCustomer(ResultSet resultSet) {
    try {
      String firstName = resultSet.getString("first_name");
      String lastName = resultSet.getString("last_name");
      String address = resultSet.getString("address");
      String phone = resultSet.getString("phone");
      String email = resultSet.getString("email");
      String id = resultSet.getString("id");
      String healthInsuranceProvider = resultSet.getString("health_insurance_provider");
      Activity activity = Activity.valueOf(resultSet.getString("activity"));

      List<String> workoutRoutines = new ArrayList<>();
      try (Connection con = dataSource.getConnection()) {
        PreparedStatement insert =
            con.prepareCall(
                "SELECT * FROM " + CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME + " WHERE CUSTOMERID = ?");
        insert.setString(1, id);
        ResultSet resultSetWorkouts = insert.executeQuery();
        while (resultSetWorkouts.next()) {
          workoutRoutines.add(resultSetWorkouts.getString("WORKOUTROUTINEID"));
        }
        insert.close();
      }

      Customer customer =
          new CustomerImpl(
              id,
              address,
              firstName,
              lastName,
              phone,
              email,
              healthInsuranceProvider,
              workoutRoutines,
              activity);

      LOGGER.trace("Got customer {}", customer);
      return customer;
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

}
