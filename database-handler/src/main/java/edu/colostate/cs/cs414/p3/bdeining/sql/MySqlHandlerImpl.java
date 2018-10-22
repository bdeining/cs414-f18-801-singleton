package edu.colostate.cs.cs414.p3.bdeining.sql;

import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.CUSTOMER_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.EXERCISE_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.EXERCISE_WORKOUT_ROUTINE_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.MACHINE_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.QUALIFICATION_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TABLES;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TABLES_DEF;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.TRAINER_TABLE_NAME;
import static edu.colostate.cs.cs414.p3.bdeining.sql.TableConstants.WORKOUT_ROUTINE_TABLE_NAME;

import edu.colostate.cs.cs414.p3.bdeining.api.Activity;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.MySqlHandler;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import edu.colostate.cs.cs414.p3.bdeining.impl.CustomerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.ExerciseImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.MachineImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.TrainerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.WorkoutRoutineImpl;
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
  name = "MySqlHandlerImpl", //
  property = { //
    "service.exported.interfaces=*", //
  }
)

// TODO : Prepared Statements
// TODO : deduplicate
// TODO: break up handlers
public class MySqlHandlerImpl implements MySqlHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(MySqlHandlerImpl.class);

  private DataSource dataSource;

  public void init() {
    LOGGER.trace("Initializing {}", MySqlHandlerImpl.class.getName());
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

    return true;
  }

  @Override
  public boolean addMachine(Machine machine) throws SQLException {
    String id = machine.getId();
    String name = machine.getName();
    String picture = machine.getPicture();
    int quantity = machine.getQuantity();

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

    return true;
  }

  @Override
  public boolean addExercise(Exercise exercise) throws SQLException {
    String id = exercise.getId();
    String name = exercise.getCommonName();
    int duration = exercise.getDurationPerSet();
    int sets = exercise.getSets();
    String machineId = exercise.getMachineId();

    try (Connection con = dataSource.getConnection()) {
      LOGGER.trace("Adding Exercise : {}", exercise);
      PreparedStatement insert =
          con.prepareStatement(
              "INSERT INTO "
                  + EXERCISE_TABLE_NAME
                  + " (name, id, machineId, sets, duration) VALUES (?,?,?,?,?)");
      insert.setString(1, name);
      insert.setString(2, id);
      insert.setString(3, machineId);
      insert.setInt(4, sets);
      insert.setInt(5, duration);
      insert.execute();
      insert.close();
    }

    return true;
  }

  @Override
  public boolean addTrainer(Trainer trainer) throws SQLException {
    String address = trainer.getAddress();
    String firstName = trainer.getFirstName();
    String lastName = trainer.getLastName();
    String phone = trainer.getPhone();
    String healthInsuranceProvider = trainer.getHealthInsuranceProvider();
    String email = trainer.getEmail();
    String id = trainer.getId();
    int workHours = trainer.getWorkHours();
    List<String> qualifications = trainer.getQualifications();

    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Adding trainer : {}", trainer);
      stmt.execute(
          String.format(
              "INSERT INTO %s (first_name, last_name, address, phone, email, id, health_insurance_provider, work_hours) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
              TRAINER_TABLE_NAME,
              firstName,
              lastName,
              address,
              phone,
              email,
              id,
              healthInsuranceProvider,
              workHours));

      for (String qualification : qualifications) {
        addQualification(stmt, qualification, id);
      }
    }

    return true;
  }

  @Override
  public boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) throws SQLException {
    String name = workoutRoutine.getName();
    String id = workoutRoutine.getId();
    List<String> workoutRoutineExerciseIds = workoutRoutine.getExerciseIds();

    try (Connection con = dataSource.getConnection()) {
      LOGGER.trace("Adding workoutRoutine : {}", workoutRoutine);

      PreparedStatement insert =
          con.prepareStatement(
              "INSERT INTO " + WORKOUT_ROUTINE_TABLE_NAME + " (name, id) VALUES (?,?)");
      insert.setString(1, name);
      insert.setString(2, id);
      insert.execute();
      insert.close();

      for (String exerciseId : workoutRoutineExerciseIds) {
        insert =
            con.prepareStatement(
                "INSERT INTO "
                    + EXERCISE_WORKOUT_ROUTINE_TABLE_NAME
                    + " (workoutRoutineId, exerciseId) VALUES (?,?)");
        insert.setString(1, id);
        insert.setString(2, exerciseId);
        insert.execute();
        insert.close();
      }
    }

    return true;
  }

  @Override
  public List<WorkoutRoutine> getWorkoutRoutines() throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(String.format("SELECT * FROM %s;", WORKOUT_ROUTINE_TABLE_NAME));

      List<WorkoutRoutine> workoutRoutineList = new ArrayList<>();
      while (resultSet.next()) {
        WorkoutRoutine workoutRoutine = getWorkoutRoutine(resultSet);
        if (workoutRoutine != null) {
          workoutRoutineList.add(workoutRoutine);
        }
      }
      return workoutRoutineList;
    }
  }

  private WorkoutRoutine getWorkoutRoutine(ResultSet resultSet) {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      String id = resultSet.getString("id");
      String name = resultSet.getString("name");

      ResultSet exerciseResultSet =
          stmt.executeQuery(
              String.format(
                  "SELECT * FROM %s WHERE workoutRoutineId = '%s';",
                  EXERCISE_WORKOUT_ROUTINE_TABLE_NAME, id));

      List<String> exerciseIds = new ArrayList<>();
      while (exerciseResultSet.next()) {
        exerciseIds.add(exerciseResultSet.getString("exerciseId"));
      }

      return new WorkoutRoutineImpl(id, name, exerciseIds);
    } catch (SQLException e) {
      LOGGER.debug("Could not get routine", e);
      return null;
    }
  }

  @Override
  public boolean removeWorkoutRoutine(String id) throws SQLException {
    removeById(id, WORKOUT_ROUTINE_TABLE_NAME);
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", EXERCISE_WORKOUT_ROUTINE_TABLE_NAME, id);
      stmt.execute(
          String.format(
              "DELETE FROM %s WHERE workoutId = '%s';", EXERCISE_WORKOUT_ROUTINE_TABLE_NAME, id));
    }

    return true;
  }

  @Override
  public List<Exercise> getExercises() throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(String.format("SELECT * FROM %s;", EXERCISE_TABLE_NAME));

      List<Exercise> exerciseList = new ArrayList<>();
      while (resultSet.next()) {
        Exercise exercise = getExercise(resultSet);
        if (exercise != null) {
          exerciseList.add(exercise);
        }
      }
      return exerciseList;
    }
  }

  private Exercise getExercise(ResultSet resultSet) {
    try {
      String id = resultSet.getString("id");
      String name = resultSet.getString("name");
      String machineId = resultSet.getString("machineId");
      int sets = resultSet.getInt("sets");
      int duration = resultSet.getInt("duration");

      return new ExerciseImpl(id, name, machineId, sets, duration);
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  @Override
  public boolean removeExercise(String id) throws SQLException {
    removeById(id, EXERCISE_TABLE_NAME);
    return true;
  }

  @Override
  public List<Machine> getMachines() throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(String.format("SELECT * FROM %s;", MACHINE_TABLE_NAME));

      List<Machine> machineList = new ArrayList<>();
      while (resultSet.next()) {
        Machine machine = getMachine(resultSet);
        if (machine != null) {
          machineList.add(machine);
        }
      }
      return machineList;
    }
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

  @Override
  public boolean removeMachine(String id) throws SQLException {
    removeById(id, MACHINE_TABLE_NAME);
    return true;
  }

  @Override
  public boolean removeCustomer(String id) throws SQLException {
    removeById(id, CUSTOMER_TABLE_NAME);
    removeById("customerId", id, CUSTOMER_WORKOUT_ROUTINE_TABLE_NAME);
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

  @Override
  public boolean removeTrainer(String trainerId) throws SQLException {
    removeById(trainerId, TRAINER_TABLE_NAME);
    removeById(trainerId, QUALIFICATION_TABLE_NAME);
    return true;
  }

  @Override
  public List<Trainer> getTrainers() throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(String.format("SELECT * FROM %s;", TRAINER_TABLE_NAME));

      List<Trainer> trainers = new ArrayList<>();
      while (resultSet.next()) {
        Trainer trainer = getTrainer(resultSet);
        if (trainer != null) {
          trainers.add(trainer);
        }
      }
      return trainers;
    }
  }

  private Trainer getTrainer(ResultSet resultSet) {
    try {
      String firstName = resultSet.getString("first_name");
      String lastName = resultSet.getString("last_name");
      String address = resultSet.getString("address");
      String phone = resultSet.getString("phone");
      String email = resultSet.getString("email");
      String id = resultSet.getString("id");
      String healthInsuranceProvider = resultSet.getString("health_insurance_provider");
      int workHours = resultSet.getInt("work_hours");
      List<String> qualifications = getQualificationsForTrainer(id);
      Trainer trainer =
          new TrainerImpl(
              id,
              address,
              firstName,
              lastName,
              phone,
              email,
              healthInsuranceProvider,
              workHours,
              qualifications);
      LOGGER.trace("Got trainer {}", trainer);
      return trainer;
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  private List<String> getQualificationsForTrainer(String id) {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {

      ResultSet resultSet =
          stmt.executeQuery(
              String.format("SELECT * from %s WHERE ID = '%s';", QUALIFICATION_TABLE_NAME, id));

      List<String> qualifications = new ArrayList<>();
      while (resultSet.next()) {
        String qualificaiton = resultSet.getString("qualification");
        qualifications.add(qualificaiton);
      }
      return qualifications;
    } catch (SQLException e) {
      LOGGER.error("Could not remove trainer {}", id, e);
    }

    return new ArrayList<>();
  }

  private void removeById(String id, String tableName) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE ID = '%s';", tableName, id));
    }
  }

  private void removeById(String idFieldName, String id, String tableName) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE %s = '%s';", tableName, idFieldName, id));
    }
  }

  private void addQualification(Statement statement, String qualification, String trainerId)
      throws SQLException {
    LOGGER.trace("Adding qualification {} to trainer with id {}", qualification, trainerId);
    statement.execute(
        String.format(
            "INSERT INTO %s (id, qualification) VALUES ('%s', '%s');",
            QUALIFICATION_TABLE_NAME, trainerId, qualification));
  }
}
