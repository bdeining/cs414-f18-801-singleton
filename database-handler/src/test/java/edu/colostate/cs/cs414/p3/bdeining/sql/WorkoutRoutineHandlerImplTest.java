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

import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
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

public class WorkoutRoutineHandlerImplTest {

  private WorkoutRoutineHandlerImpl workoutRoutineHandler;

  private DataSource dataSource;

  private DatabaseMetaData databaseMetaData;

  private Connection connection;

  private Statement statement;

  private PreparedStatement preparedStatement;

  private CallableStatement callableStatement;

  @Before
  public void setUp() throws Exception {
    setUpMocks();
    workoutRoutineHandler = new WorkoutRoutineHandlerImpl();
    workoutRoutineHandler.setDataSource(dataSource);
  }

  @Test
  public void testInit() throws Exception {
    verify(connection, times(1)).getMetaData();
    verify(databaseMetaData, times(1)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(2)).execute(anyString());
  }

  @Test
  public void testInitWithTable() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(databaseMetaData.getTables(anyString(), anyString(), anyString(), any()))
        .thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("TABLE_NAME")).thenReturn(TableConstants.CUSTOMER_TABLE_NAME);

    workoutRoutineHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(4)).execute(anyString());
  }

  @Test
  public void testInitGetMetadataSqlException() throws Exception {
    when(connection.getMetaData()).thenThrow(SQLException.class);
    workoutRoutineHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(1)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(4)).execute(anyString());
  }

  @Test
  public void testInitConnectionSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(databaseMetaData.getTables(anyString(), anyString(), anyString(), any()))
        .thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("TABLE_NAME")).thenReturn(TableConstants.CUSTOMER_TABLE_NAME);
    when(statement.execute(anyString())).thenThrow(SQLException.class);
    workoutRoutineHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(4)).execute(anyString());
  }

  @Test
  public void testAddWorkoutRoutine() throws Exception {
    boolean result = workoutRoutineHandler.addWorkoutRoutine(getMockWorkoutRoutine());
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

    List<WorkoutRoutine> workoutRoutineList = workoutRoutineHandler.getWorkoutRoutines();
    assertThat(workoutRoutineList, hasSize(1));
    assertThat(workoutRoutineList.get(0).getId(), is("anId"));
    assertThat(workoutRoutineList.get(0).getName(), is("aName"));
    assertThat(workoutRoutineList.get(0).getExerciseIds(), hasSize(1));
    assertThat(workoutRoutineList.get(0).getExerciseIds().get(0), is("exerciseId"));
  }

  @Test
  public void testGetWorkoutRoutineExceptionRoutine() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true).thenThrow(SQLException.class).thenReturn(false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("aName");
    when(tableResultSet.getString("exerciseId")).thenReturn("exerciseId");

    List<WorkoutRoutine> workoutRoutineList = workoutRoutineHandler.getWorkoutRoutines();
    assertThat(workoutRoutineList, hasSize(0));
  }

  @Test
  public void testRemoveWorkoutRoutine() throws Exception {
    boolean result = workoutRoutineHandler.removeWorkoutRoutine("anId");
    assertThat(result, is(true));
    verify(statement, times(4)).execute(anyString());
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

  private WorkoutRoutine getMockWorkoutRoutine() {
    return new WorkoutRoutineImpl(
        UUID.randomUUID().toString(), "aName", Arrays.asList(UUID.randomUUID().toString()));
  }
}
