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

import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import edu.colostate.cs.cs414.p3.bdeining.impl.TrainerImpl;
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

public class TrainerHandlerImplTest {

  private TrainerHandlerImpl trainerHandler;

  private DataSource dataSource;

  private DatabaseMetaData databaseMetaData;

  private Connection connection;

  private Statement statement;

  private PreparedStatement preparedStatement;

  private CallableStatement callableStatement;

  @Before
  public void setUp() throws Exception {
    setUpMocks();
    trainerHandler = new TrainerHandlerImpl();
    trainerHandler.setDataSource(dataSource);
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

    trainerHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(4)).execute(anyString());
  }

  @Test
  public void testInitGetMetadataSqlException() throws Exception {
    when(connection.getMetaData()).thenThrow(SQLException.class);
    trainerHandler.init();

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
    trainerHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(4)).execute(anyString());
  }

  @Test
  public void testAddTrainer() throws Exception {
    boolean result = trainerHandler.addTrainer(getMockTrainer());
    verify(statement, times(2)).execute(anyString());
    verify(preparedStatement, times(2)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testRemoveTrainer() throws Exception {
    boolean result = trainerHandler.removeTrainer("anId");
    assertThat(result, is(true));
    verify(statement, times(4)).execute(anyString());
  }

  @Test
  public void testGetTrainers() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(tableResultSet);
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
    when(tableResultSet.getString("password")).thenReturn("password");

    when(callableStatement.executeQuery()).thenReturn(tableResultSet);

    List<Trainer> trainerList = trainerHandler.getTrainers();
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
    assertThat(trainerList.get(0).getPassword(), is("password"));
  }

  @Test
  public void testGetTrainersSQLException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("id")).thenThrow(SQLException.class);

    when(callableStatement.executeQuery()).thenReturn(tableResultSet);

    List<Trainer> trainerList = trainerHandler.getTrainers();
    assertThat(trainerList, hasSize(0));
  }

  @Test
  public void testGetTrainersSQLExceptionQualification() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(tableResultSet);
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
    when(tableResultSet.getString("password")).thenReturn("password");

    List<Trainer> trainerList = trainerHandler.getTrainers();
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
    assertThat(trainerList.get(0).getPassword(), is("password"));
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
        Arrays.asList(UUID.randomUUID().toString()),
        "password");
  }
}
