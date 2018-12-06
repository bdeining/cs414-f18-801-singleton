package edu.colostate.cs.cs414.p3.bdeining.sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colostate.cs.cs414.p3.bdeining.api.Activity;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;

public class FactoryTest {

  private DataSource dataSource;

  private DatabaseMetaData databaseMetaData;

  private Connection connection;

  private Statement statement;

  private PreparedStatement preparedStatement;

  private CallableStatement callableStatement;

  @Before
  public void setUp() throws Exception {
    setUpMocks();
  }

  @Test
  public void testCreateTrainer() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("address")).thenReturn("address");
    when(tableResultSet.getString("first_name")).thenReturn("ben");
    when(tableResultSet.getString("last_name")).thenReturn("deininger");
    when(tableResultSet.getString("phone")).thenReturn("123-4567");
    when(tableResultSet.getString("email")).thenReturn("ben@example.com");
    when(tableResultSet.getString("branch")).thenReturn("branch");
    when(tableResultSet.getString("health_insurance_provider")).thenReturn("kaiser");
    when(tableResultSet.getString("qualification")).thenReturn("qual1");
    when(tableResultSet.getInt("work_hours")).thenReturn(12);

    when(preparedStatement.executeQuery()).thenReturn(tableResultSet);

    Trainer trainer = Factory.createTrainer(dataSource, tableResultSet);

    assertThat(trainer.getId(), is("anId"));
    assertThat(trainer.getAddress(), is("address"));
    assertThat(trainer.getFirstName(), is("ben"));
    assertThat(trainer.getLastName(), is("deininger"));
    assertThat(trainer.getPhone(), is("123-4567"));
    assertThat(trainer.getEmail(), is("ben@example.com"));
    assertThat(trainer.getHealthInsuranceProvider(), is("kaiser"));
    assertThat(trainer.getBranch(), is("branch"));
    assertThat(trainer.getWorkHours(), is(12));
  }

  @Test
  public void testCreateTrainerSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("id")).thenThrow(SQLException.class);

    when(preparedStatement.executeQuery()).thenReturn(tableResultSet);

    Trainer trainer = Factory.createTrainer(dataSource, tableResultSet);

    assertThat(trainer, nullValue());
  }

  @Test
  public void testCreateCustomer() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("address")).thenReturn("address");
    when(tableResultSet.getString("first_name")).thenReturn("ben");
    when(tableResultSet.getString("last_name")).thenReturn("deininger");
    when(tableResultSet.getString("phone")).thenReturn("123-4567");
    when(tableResultSet.getString("email")).thenReturn("ben@example.com");
    when(tableResultSet.getString("branch")).thenReturn("branch");
    when(tableResultSet.getString("health_insurance_provider")).thenReturn("kaiser");
    when(tableResultSet.getString("activity")).thenReturn(Activity.ACTIVE.toString());
    when(tableResultSet.getString("WORKOUTROUTINEID")).thenReturn("WORKOUTROUTINEID");
    when(callableStatement.executeQuery()).thenReturn(tableResultSet);

    Customer customer = Factory.createCustomer(dataSource, tableResultSet);

    assertThat(customer.getId(), is("anId"));
    assertThat(customer.getAddress(), is("address"));
    assertThat(customer.getFirstName(), is("ben"));
    assertThat(customer.getLastName(), is("deininger"));
    assertThat(customer.getPhone(), is("123-4567"));
    assertThat(customer.getEmail(), is("ben@example.com"));
    assertThat(customer.getHealthInsuranceProvider(), is("kaiser"));
    assertThat(customer.getBranch(), is("branch"));
    assertThat(customer.getActivity(), is(Activity.ACTIVE));
    assertThat(customer.getWorkoutRoutineIds(), hasSize(1));
    assertThat(customer.getWorkoutRoutineIds().get(0), is("WORKOUTROUTINEID"));
  }

  @Test
  public void testCreateCustomerSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("address")).thenReturn("address");
    when(tableResultSet.getString("first_name")).thenReturn("ben");
    when(tableResultSet.getString("last_name")).thenReturn("deininger");
    when(tableResultSet.getString("phone")).thenReturn("123-4567");
    when(tableResultSet.getString("email")).thenReturn("ben@example.com");
    when(tableResultSet.getString("branch")).thenReturn("branch");
    when(tableResultSet.getString("health_insurance_provider")).thenReturn("kaiser");
    when(tableResultSet.getString("activity")).thenReturn(Activity.ACTIVE.toString());
    when(tableResultSet.getString("WORKOUTROUTINEID")).thenReturn("WORKOUTROUTINEID");
    when(callableStatement.executeQuery()).thenThrow(SQLException.class);

    Customer customer = Factory.createCustomer(dataSource, tableResultSet);
    assertThat(customer, nullValue());
  }

  @Test
  public void testCreateExercise() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("name");
    when(tableResultSet.getString("machineId")).thenReturn("123");
    when(tableResultSet.getString("branch")).thenReturn("branch");
    when(tableResultSet.getInt("sets")).thenReturn(2);
    when(tableResultSet.getInt("duration")).thenReturn(12);

    Exercise exercise = Factory.createExercise(tableResultSet);

    assertThat(exercise.getId(), is("anId"));
    assertThat(exercise.getCommonName(), is("name"));
    assertThat(exercise.getMachineId(), is("123"));
    assertThat(exercise.getBranch(), is("branch"));
    assertThat(exercise.getDurationPerSet(), is(12));
    assertThat(exercise.getSets(), is(2));
  }

  @Test
  public void testCreateExerciseSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.getString("id")).thenThrow(SQLException.class);

    Exercise exercise = Factory.createExercise(tableResultSet);

    assertThat(exercise, nullValue());
  }

  @Test
  public void testCreateMachine() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("name");
    when(tableResultSet.getString("picture")).thenReturn("123");
    when(tableResultSet.getInt("quantity")).thenReturn(2);
    when(tableResultSet.getString("branch")).thenReturn("branch");

    Machine machine = Factory.createMachine(tableResultSet);

    assertThat(machine.getId(), is("anId"));
    assertThat(machine.getName(), is("name"));
    assertThat(machine.getPicture(), is("123"));
    assertThat(machine.getBranch(), is("branch"));
    assertThat(machine.getQuantity(), is(2));
  }

  @Test
  public void testCreateMachineSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.getString("id")).thenThrow(SQLException.class);

    Machine machine = Factory.createMachine(tableResultSet);

    assertThat(machine, nullValue());
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
}
