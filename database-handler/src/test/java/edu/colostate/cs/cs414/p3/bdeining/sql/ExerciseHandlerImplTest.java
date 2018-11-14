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

import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.impl.ExerciseImpl;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;

public class ExerciseHandlerImplTest {
  private ExerciseHandlerImpl exerciseHandler;

  private DataSource dataSource;

  private DatabaseMetaData databaseMetaData;

  private Connection connection;

  private Statement statement;

  private PreparedStatement preparedStatement;

  @Before
  public void setUp() throws Exception {
    setUpMocks();
    exerciseHandler = new ExerciseHandlerImpl();
    exerciseHandler.setDataSource(dataSource);
  }

  @Test
  public void testInit() throws Exception {
    verify(connection, times(1)).getMetaData();
    verify(databaseMetaData, times(1)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(1)).execute(anyString());
  }

  @Test
  public void testInitWithTable() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(databaseMetaData.getTables(anyString(), anyString(), anyString(), any()))
        .thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("TABLE_NAME")).thenReturn(TableConstants.CUSTOMER_TABLE_NAME);

    exerciseHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(2)).execute(anyString());
  }

  @Test
  public void testInitGetMetadataSqlException() throws Exception {
    when(connection.getMetaData()).thenThrow(SQLException.class);
    exerciseHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(1)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(2)).execute(anyString());
  }

  @Test
  public void testInitConnectionSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(databaseMetaData.getTables(anyString(), anyString(), anyString(), any()))
        .thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("TABLE_NAME")).thenReturn(TableConstants.CUSTOMER_TABLE_NAME);
    when(statement.execute(anyString())).thenThrow(SQLException.class);
    exerciseHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(2)).execute(anyString());
  }

  @Test
  public void testAddExercise() throws Exception {
    boolean result = exerciseHandler.addExercise(getMockExercise());
    verify(preparedStatement, times(1)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testGetExercises() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("aName");
    when(tableResultSet.getString("machineId")).thenReturn("machineId");
    when(tableResultSet.getInt("duration")).thenReturn(2);
    when(tableResultSet.getInt("sets")).thenReturn(3);

    List<Exercise> exerciseList = exerciseHandler.getExercises();
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

    List<Exercise> exerciseList = exerciseHandler.getExercises();
    assertThat(exerciseList, hasSize(0));
  }

  @Test
  public void testRemoveExercise() throws Exception {
    boolean result = exerciseHandler.removeExercise("anId");
    assertThat(result, is(true));
    verify(statement, times(2)).execute(anyString());
  }

  private void setUpMocks() throws Exception {
    dataSource = mock(DataSource.class);
    connection = mock(Connection.class);
    databaseMetaData = mock(DatabaseMetaData.class);
    statement = mock(Statement.class);
    preparedStatement = mock(PreparedStatement.class);

    ResultSet tableResultSet = mock(ResultSet.class);
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.getMetaData()).thenReturn(databaseMetaData);
    when(databaseMetaData.getTables(anyString(), anyString(), anyString(), any()))
        .thenReturn(tableResultSet);
    when(connection.createStatement()).thenReturn(statement);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
  }

  private Exercise getMockExercise() {
    return new ExerciseImpl(UUID.randomUUID().toString(), "aMachine", "12345", 2, 3);
  }
}
