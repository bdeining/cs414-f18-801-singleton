package edu.colostate.cs.cs414.p3.bdeining.sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.colostate.cs.cs414.p3.bdeining.api.Activity;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import edu.colostate.cs.cs414.p3.bdeining.impl.CustomerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.ExerciseImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.MachineImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.TrainerImpl;
import edu.colostate.cs.cs414.p3.bdeining.impl.WorkoutRoutineImpl;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;

public class MySqlHandlerImplTest {

  private MySqlHandlerImpl mySqlHandler;

  private DataSource dataSource;

  private DatabaseMetaData databaseMetaData;

  private Connection connection;

  private Statement statement;

  private PreparedStatement preparedStatement;

  private CallableStatement callableStatement;

  @Before
  public void setUp() throws Exception {
    setUpMocks();
    mySqlHandler = new MySqlHandlerImpl();
    mySqlHandler.setDataSource(dataSource);
  }

  @Test
  public void testInit() throws Exception {
    verify(connection, times(1)).getMetaData();
    verify(databaseMetaData, times(1)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(8)).execute(anyString());
  }

  @Test
  public void testInitWithTable() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(databaseMetaData.getTables(anyString(), anyString(), anyString(), any()))
        .thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("TABLE_NAME")).thenReturn(TableConstants.CUSTOMER_TABLE_NAME);

    mySqlHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(15)).execute(anyString());
  }

  @Test
  public void testInitGetMetadataSqlException() throws Exception {
    when(connection.getMetaData()).thenThrow(SQLException.class);
    mySqlHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(1)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(16)).execute(anyString());
  }

  @Test
  public void testInitConnectionSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(databaseMetaData.getTables(anyString(), anyString(), anyString(), any()))
        .thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("TABLE_NAME")).thenReturn(TableConstants.CUSTOMER_TABLE_NAME);
    when(statement.execute(anyString())).thenThrow(SQLException.class);
    mySqlHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(15)).execute(anyString());
  }

  @Test
  public void testAddCustomer() throws Exception {
    boolean result = mySqlHandler.addCustomer(getMockCustomer());
    verify(preparedStatement, times(2)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testAddMachine() throws Exception {
    boolean result = mySqlHandler.addMachine(getMockMachine());
    verify(preparedStatement, times(1)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testAddExercise() throws Exception {
    boolean result = mySqlHandler.addExercise(getMockExercise());
    verify(preparedStatement, times(1)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testAddTrainer() throws Exception {
    boolean result = mySqlHandler.addTrainer(getMockTrainer());
    verify(statement, times(10)).execute(anyString());
    assertThat(result, is(true));
  }

  @Test
  public void testAddWorkoutRoutine() throws Exception {
    boolean result = mySqlHandler.addWorkoutRoutine(getMockWorkoutRoutine());
    verify(preparedStatement, times(2)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testGetWorkoutRoutine() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("aName");
    when(tableResultSet.getString("exerciseId")).thenReturn("exerciseId");

    List<WorkoutRoutine> workoutRoutineList = mySqlHandler.getWorkoutRoutines();
    assertThat(workoutRoutineList, hasSize(1));
    assertThat(workoutRoutineList.get(0).getId(), is("anId"));
    assertThat(workoutRoutineList.get(0).getName(), is("aName"));
    assertThat(workoutRoutineList.get(0).getExerciseIds(), hasSize(1));
    assertThat(workoutRoutineList.get(0).getExerciseIds().get(0), is("exerciseId"));
  }

  @Test
  public void testGetExercises() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("aName");
    when(tableResultSet.getString("machineId")).thenReturn("machineId");
    when(tableResultSet.getInt("duration")).thenReturn(2);
    when(tableResultSet.getInt("sets")).thenReturn(3);

    List<Exercise> exerciseList = mySqlHandler.getExercises();
    assertThat(exerciseList, hasSize(1));
    assertThat(exerciseList.get(0).getId(), is("anId"));
    assertThat(exerciseList.get(0).getCommonName(), is("aName"));
    assertThat(exerciseList.get(0).getDurationPerSet(), is(2));
    assertThat(exerciseList.get(0).getSets(), is(3));
    assertThat(exerciseList.get(0).getMachineId(), is("machineId"));
  }

  @Test
  public void testGetExercisesSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true).thenReturn(false);
    when(tableResultSet.getString(anyString())).thenThrow(SQLException.class);

    List<Exercise> exerciseList = mySqlHandler.getExercises();
    assertThat(exerciseList, hasSize(0));
  }

  @Test
  public void testGetWorkoutRoutineExceptionRoutine() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true).thenThrow(SQLException.class).thenReturn(false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("aName");
    when(tableResultSet.getString("exerciseId")).thenReturn("exerciseId");

    List<WorkoutRoutine> workoutRoutineList = mySqlHandler.getWorkoutRoutines();
    assertThat(workoutRoutineList, hasSize(0));
  }

  @Test
  public void testRemoveWorkoutRoutine() throws Exception {
    boolean result = mySqlHandler.removeWorkoutRoutine("anId");
    assertThat(result, is(true));
    verify(statement, times(10)).execute(anyString());
  }

  @Test
  public void testRemoveExercise() throws Exception {
    boolean result = mySqlHandler.removeExercise("anId");
    assertThat(result, is(true));
    verify(statement, times(9)).execute(anyString());
  }

  @Test
  public void testRemoveMachine() throws Exception {
    boolean result = mySqlHandler.removeMachine("anId");
    assertThat(result, is(true));
    verify(statement, times(9)).execute(anyString());
  }

  @Test
  public void testRemoveTrainer() throws Exception {
    boolean result = mySqlHandler.removeTrainer("anId");
    assertThat(result, is(true));
    verify(statement, times(10)).execute(anyString());
  }

  @Test
  public void testRemoveCustomer() throws Exception {
    boolean result = mySqlHandler.removeCustomer("anId");
    assertThat(result, is(true));
    verify(statement, times(10)).execute(anyString());
  }

  @Test
  public void testGetMachines() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("aName");
    when(tableResultSet.getString("picture")).thenReturn("picture");
    when(tableResultSet.getInt("quantity")).thenReturn(12);

    List<Machine> machineList = mySqlHandler.getMachines();
    assertThat(machineList, hasSize(1));
    assertThat(machineList.get(0).getId(), is("anId"));
    assertThat(machineList.get(0).getName(), is("aName"));
    assertThat(machineList.get(0).getPicture(), is("picture"));
    assertThat(machineList.get(0).getQuantity(), is(12));
  }

  @Test
  public void testGetMachinesSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString(anyString())).thenThrow(SQLException.class);

    List<Machine> machineList = mySqlHandler.getMachines();
    assertThat(machineList, hasSize(0));
  }

  @Test
  public void testGetCustomers() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("address")).thenReturn("address");
    when(tableResultSet.getString("first_name")).thenReturn("ben");
    when(tableResultSet.getString("last_name")).thenReturn("deininger");
    when(tableResultSet.getString("phone")).thenReturn("123-4567");
    when(tableResultSet.getString("email")).thenReturn("ben@example.com");
    when(tableResultSet.getString("health_insurance_provider")).thenReturn("kaiser");
    when(tableResultSet.getString("activity")).thenReturn(Activity.ACTIVE.toString());
    when(tableResultSet.getString("WORKOUTROUTINEID")).thenReturn("WORKOUTROUTINEID");

    when(callableStatement.executeQuery()).thenReturn(tableResultSet);

    List<Customer> customerList = mySqlHandler.getCustomers();
    assertThat(customerList, hasSize(1));
    assertThat(customerList.get(0).getId(), is("anId"));
    assertThat(customerList.get(0).getAddress(), is("address"));
    assertThat(customerList.get(0).getFirstName(), is("ben"));
    assertThat(customerList.get(0).getLastName(), is("deininger"));
    assertThat(customerList.get(0).getPhone(), is("123-4567"));
    assertThat(customerList.get(0).getEmail(), is("ben@example.com"));
    assertThat(customerList.get(0).getHealthInsuranceProvider(), is("kaiser"));
    assertThat(customerList.get(0).getActivity(), is(Activity.ACTIVE));
    assertThat(customerList.get(0).getWorkoutRoutineIds(), hasSize(1));
    assertThat(customerList.get(0).getWorkoutRoutineIds().get(0), is("WORKOUTROUTINEID"));
  }

  @Test
  public void testGetTrainers() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("address")).thenReturn("address");
    when(tableResultSet.getString("first_name")).thenReturn("ben");
    when(tableResultSet.getString("last_name")).thenReturn("deininger");
    when(tableResultSet.getString("phone")).thenReturn("123-4567");
    when(tableResultSet.getString("email")).thenReturn("ben@example.com");
    when(tableResultSet.getString("health_insurance_provider")).thenReturn("kaiser");
    when(tableResultSet.getInt("work_hours")).thenReturn(12);
    when(tableResultSet.getString("qualification")).thenReturn("qualification");

    when(callableStatement.executeQuery()).thenReturn(tableResultSet);

    List<Trainer> trainerList = mySqlHandler.getTrainers();
    assertThat(trainerList, hasSize(1));
    assertThat(trainerList.get(0).getId(), is("anId"));
    assertThat(trainerList.get(0).getAddress(), is("address"));
    assertThat(trainerList.get(0).getFirstName(), is("ben"));
    assertThat(trainerList.get(0).getLastName(), is("deininger"));
    assertThat(trainerList.get(0).getPhone(), is("123-4567"));
    assertThat(trainerList.get(0).getEmail(), is("ben@example.com"));
    assertThat(trainerList.get(0).getHealthInsuranceProvider(), is("kaiser"));
    assertThat(trainerList.get(0).getWorkHours(), is(12));
    assertThat(trainerList.get(0).getQualifications(), hasSize(1));
    assertThat(trainerList.get(0).getQualifications().get(0), is("qualification"));
  }

  @Test
  public void testGetTrainersSQLException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("id")).thenThrow(SQLException.class);

    when(callableStatement.executeQuery()).thenReturn(tableResultSet);

    List<Trainer> trainerList = mySqlHandler.getTrainers();
    assertThat(trainerList, hasSize(0));
  }

  @Test
  public void testGetTrainersSQLExceptionQualification() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true).thenThrow(SQLException.class).thenReturn(false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("address")).thenReturn("address");
    when(tableResultSet.getString("first_name")).thenReturn("ben");
    when(tableResultSet.getString("last_name")).thenReturn("deininger");
    when(tableResultSet.getString("phone")).thenReturn("123-4567");
    when(tableResultSet.getString("email")).thenReturn("ben@example.com");
    when(tableResultSet.getString("health_insurance_provider")).thenReturn("kaiser");
    when(tableResultSet.getInt("work_hours")).thenReturn(12);
    when(tableResultSet.getString("qualification")).thenReturn("qualification");

    List<Trainer> trainerList = mySqlHandler.getTrainers();
    assertThat(trainerList, hasSize(1));
    assertThat(trainerList.get(0).getId(), is("anId"));
    assertThat(trainerList.get(0).getAddress(), is("address"));
    assertThat(trainerList.get(0).getFirstName(), is("ben"));
    assertThat(trainerList.get(0).getLastName(), is("deininger"));
    assertThat(trainerList.get(0).getPhone(), is("123-4567"));
    assertThat(trainerList.get(0).getEmail(), is("ben@example.com"));
    assertThat(trainerList.get(0).getHealthInsuranceProvider(), is("kaiser"));
    assertThat(trainerList.get(0).getWorkHours(), is(12));
    assertThat(trainerList.get(0).getQualifications(), hasSize(0));
  }

  @Test
  public void testGetCustomersSQLException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("id")).thenThrow(SQLException.class);

    when(callableStatement.executeQuery()).thenReturn(tableResultSet);

    List<Customer> customerList = mySqlHandler.getCustomers();
    assertThat(customerList, hasSize(0));
  }

  private void setUpMocks() throws Exception {
    dataSource = mock(DataSource.class);
    connection = mock(Connection.class);
    databaseMetaData = mock(DatabaseMetaData.class);
    statement = mock(Statement.class);
    preparedStatement = mock(PreparedStatement.class);
    callableStatement = mock(CallableStatement.class);
    ResultSet tableResultSet = mock(ResultSet.class);
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.getMetaData()).thenReturn(databaseMetaData);
    when(databaseMetaData.getTables(anyString(), anyString(), anyString(), any()))
        .thenReturn(tableResultSet);
    when(connection.createStatement()).thenReturn(statement);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(connection.prepareCall(anyString())).thenReturn(callableStatement);
  }

  private Customer getMockCustomer() {
    return new CustomerImpl(
        UUID.randomUUID().toString(),
        "anAddress",
        "Ben",
        "Deininger",
        "123-4123",
        "ben@example.com",
        "kaiser",
        Arrays.asList(UUID.randomUUID().toString()),
        Activity.ACTIVE);
  }

  private Machine getMockMachine() {
    return new MachineImpl(UUID.randomUUID().toString(), "aMachine", "12345", 2);
  }

  private Exercise getMockExercise() {
    return new ExerciseImpl(UUID.randomUUID().toString(), "aMachine", "12345", 2, 3);
  }

  private Trainer getMockTrainer() {
    return new TrainerImpl(
        UUID.randomUUID().toString(),
        "anAddress",
        "Ben",
        "Deininger",
        "123-4123",
        "ben@example.com",
        "kaiser",
        3,
        Arrays.asList(UUID.randomUUID().toString()));
  }

  private WorkoutRoutine getMockWorkoutRoutine() {
    return new WorkoutRoutineImpl(
        UUID.randomUUID().toString(), "aName", Arrays.asList(UUID.randomUUID().toString()));
  }
}
