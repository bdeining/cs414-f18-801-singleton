package edu.colostate.cs.cs414.p3.bdeining.sql;

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

  private static final String CUSTOMER_TABLE_NAME = "CUSTOMER";

  private static final String EXERCISE_TABLE_NAME = "EXERCISE";

  private static final String MACHINE_TABLE_NAME = "MACHINE";

  private static final String TRAINER_TABLE_NAME = "TRAINER";

  private static final String WORKOUT_ROUTINE_TABLE_NAME = "WORKOUT";

  private static final String QUALIFICATION_TABLE_NAME = "QUALIFICATIONS";

  private static final String EXERCISE_TABLE_DEF =
      "(name varchar(100), id varchar(100), machineId varchar(100), sets integer, duration integer, workoutRoutineId varchar(100))";

  private static final String MACHINE_TABLE_DEF =
      "(name varchar(100), id varchar(100), picture varchar(1024), quantity integer)";

  private static final String CUSTOMER_TABLE_DEF =
      "(first_name varchar(100), last_name varchar(100), address varchar(100), phone varchar(100), email varchar(100), id varchar(100), health_insurance_provider varchar(100), activity varchar(100))";

  private static final String WORKOUT_ROUTINE_TABLE_DEF = "(id varchar(100), name varchar(100))";

  private static final String QUALIFICATION_TABLE_DEF =
      "(id varchar(100), qualification varchar(100))";

  private static final String TRAINER_TABLE_DEF =
      "(first_name varchar(100), last_name varchar(100), address varchar(100), phone varchar(100), email varchar(100), id varchar(100), health_insurance_provider varchar(100), work_hours integer)";

  private DataSource dataSource;

  public void init() {
    LOGGER.trace("Initializing {}", MySqlHandlerImpl.class.getName());
    createTablesIfNonExistent();
  }

  private void createTablesIfNonExistent() {
    List<String> tables = getExistingTables();
    LOGGER.trace("Existing tables : {}", tables);

    if (!tables.contains(CUSTOMER_TABLE_NAME)) {
      createTable(CUSTOMER_TABLE_NAME, CUSTOMER_TABLE_DEF);
    }

    if (!tables.contains(EXERCISE_TABLE_NAME)) {
      createTable(EXERCISE_TABLE_NAME, EXERCISE_TABLE_DEF);
    }

    if (!tables.contains(MACHINE_TABLE_NAME)) {
      createTable(MACHINE_TABLE_NAME, MACHINE_TABLE_DEF);
    }

    if (!tables.contains(TRAINER_TABLE_NAME)) {
      createTable(TRAINER_TABLE_NAME, TRAINER_TABLE_DEF);
    }

    if (!tables.contains(WORKOUT_ROUTINE_TABLE_NAME)) {
      createTable(WORKOUT_ROUTINE_TABLE_NAME, WORKOUT_ROUTINE_TABLE_DEF);
    }

    if (!tables.contains(QUALIFICATION_TABLE_NAME)) {
      createTable(QUALIFICATION_TABLE_NAME, QUALIFICATION_TABLE_DEF);
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
  public boolean addCustomer(Customer customer) {
    return false;
  }

  @Override
  public List<WorkoutRoutine> getWorkoutRoutines() throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(String.format("SELECT * FROM %s;", WORKOUT_ROUTINE_TABLE_NAME));

      List<WorkoutRoutine> workoutRoutineList = new ArrayList<>();
      while (resultSet.next()) {
        WorkoutRoutine trainer = getWorkoutRoutine(resultSet);
        if (trainer != null) {
          workoutRoutineList.add(trainer);
        }
      }
      return workoutRoutineList;
    }
  }

  private WorkoutRoutine getWorkoutRoutine(ResultSet resultSet) {

    return null;
  }

  @Override
  public boolean removeWorkoutRoutine(String id) throws SQLException {
    return false;
  }

  @Override
  public List<Exercise> getExercies() throws SQLException {
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

      Machine machine = getMachineById(machineId);
      return new ExerciseImpl(id, name, machine, sets, duration);
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  @Override
  public boolean removeExercise(String id) throws SQLException {
    return false;
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

  private Machine getMachineById(String id) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      ResultSet resultSet =
          stmt.executeQuery(
              String.format("SELECT FROM %s WHERE ID = '%s';", MACHINE_TABLE_NAME, id));
      return getMachine(resultSet);
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
    return false;
  }

  @Override
  public boolean removeCustomer(String id) throws SQLException {
    return false;
  }

  @Override
  public boolean addMachine(Machine machine) throws SQLException {
    String id = machine.getId();
    String name = machine.getName();
    String picture = machine.getPicture();
    int quantity = machine.quantitiy();

    try (Connection con = dataSource.getConnection()) {
      LOGGER.trace("Adding machine : {}", machine);

      PreparedStatement insert =
          con.prepareStatement(
              "INSERT INTO "
                  + MACHINE_TABLE_NAME
                  + " (name, id, picture, quantity) VALUES (?,?,?,?)");
      insert.setString(1, id);
      insert.setString(2, name);
      insert.setString(3, picture);
      insert.setInt(4, quantity);
      insert.execute();
      insert.close();
    }

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

      /* TODO : Workout Routine Get
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
      */

      Customer customer =
          new CustomerImpl(
              id,
              address,
              firstName,
              lastName,
              phone,
              email,
              healthInsuranceProvider,
              new ArrayList<>(),
              activity);

      LOGGER.trace("Got customer {}", customer);
      return customer;
    } catch (SQLException e) {
      LOGGER.error("No data", e);
      return null;
    }
  }

  @Override
  public boolean addExercise(Exercise exercise) throws SQLException {
    /*String id = exercise.getId();
        String name = exercise.getCommonName();
        int duration = exercise.getDurationPerSet();
        int sets = exercise.getSets();
        Machine machine = exercise.getMachine();



        try (Connection con = dataSource.getConnection()) {
          LOGGER.trace("Adding machine : {}", machine);
    //      "(name varchar(100), id varchar(100), machineId varchar(100), sets integer, duration integer, workoutRoutineId varchar(100))";

          PreparedStatement insert = con.prepareStatement("INSERT INTO " + EXERCISE_TABLE_NAME +" (name, id, picture, quantity) VALUES (?,?,?,?)");
          insert.setString(1, id);
          insert.setString(2, name);
          insert.setString(3, picture);
          insert.setInt(4, quantity);
          insert.execute();
          insert.close();

        }*/

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
  public boolean removeTrainer(String trainerId) throws SQLException {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing trainer : {}", trainerId);
      stmt.execute(String.format("DELETE FROM %s WHERE ID = '%s';", TRAINER_TABLE_NAME, trainerId));
      removeById(trainerId, QUALIFICATION_TABLE_NAME);
    }
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
        if (qualificaiton != null && !qualificaiton.isEmpty()) {
          qualifications.add(qualificaiton);
        }
      }
      return qualifications;
    } catch (SQLException e) {
      LOGGER.error("Could not remove trainer {}", id, e);
    }

    return new ArrayList<>();
  }

  private void removeById(String id, String tableName) {
    try (Connection con = dataSource.getConnection();
        Statement stmt = con.createStatement()) {
      LOGGER.trace("Removing from table {} : {}", tableName, id);
      stmt.execute(String.format("DELETE FROM %s WHERE ID = '%s';", tableName, id));
    } catch (SQLException e) {
      LOGGER.error("Could not remove trainer {}", id, e);
    }
  }

  private void addQualification(Statement statement, String qualification, String trainerId) {
    try {
      LOGGER.trace("Adding qualification {} to trainer with id {}", qualification, trainerId);
      statement.execute(
          String.format(
              "INSERT INTO %s (id, qualification) VALUES ('%s', '%s');",
              QUALIFICATION_TABLE_NAME, trainerId, qualification));
    } catch (SQLException e) {
      LOGGER.error("Could not add qualification {} to {}", qualification, trainerId, e);
    }
  }

  @Override
  public boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) {
    return false;
  }
}
