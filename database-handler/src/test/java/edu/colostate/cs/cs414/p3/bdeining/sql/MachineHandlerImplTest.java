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

import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.impl.MachineImpl;
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

public class MachineHandlerImplTest {

  private static final String BRANCH = "branch";

  private MachineHandlerImpl machineHandler;

  private DataSource dataSource;

  private DatabaseMetaData databaseMetaData;

  private Connection connection;

  private Statement statement;

  private PreparedStatement preparedStatement;

  @Before
  public void setUp() throws Exception {
    setUpMocks();
    machineHandler = new MachineHandlerImpl();
    machineHandler.setDataSource(dataSource);
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

    machineHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(2)).execute(anyString());
  }

  @Test
  public void testInitGetMetadataSqlException() throws Exception {
    when(connection.getMetaData()).thenThrow(SQLException.class);
    machineHandler.init();

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
    machineHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(2)).execute(anyString());
  }

  @Test
  public void testAddMachine() throws Exception {
    boolean result = machineHandler.addMachine(getMockMachine());
    verify(preparedStatement, times(1)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testUpdateMachine() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);

    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("name");
    when(tableResultSet.getString("picture")).thenReturn("123");
    when(tableResultSet.getInt("quantity")).thenReturn(2);
    when(tableResultSet.getString("branch")).thenReturn("branch");

    when(tableResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
    when(preparedStatement.executeQuery()).thenReturn(tableResultSet);

    boolean result = machineHandler.addMachine(getMockMachine());
    verify(preparedStatement, times(1)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testRemoveMachine() throws Exception {
    boolean result = machineHandler.removeMachine("anId");
    assertThat(result, is(true));
    verify(statement, times(2)).execute(anyString());
  }

  @Test
  public void testGetMachines() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString("id")).thenReturn("anId");
    when(tableResultSet.getString("name")).thenReturn("aName");
    when(tableResultSet.getString("picture")).thenReturn("picture");
    when(tableResultSet.getString("branch")).thenReturn("branch");
    when(tableResultSet.getInt("quantity")).thenReturn(12);

    List<Machine> machineList = machineHandler.getMachines(BRANCH);
    assertThat(machineList, hasSize(1));
    assertThat(machineList.get(0).getId(), is("anId"));
    assertThat(machineList.get(0).getName(), is("aName"));
    assertThat(machineList.get(0).getPicture(), is("picture"));
    assertThat(machineList.get(0).getBranch(), is("branch"));
    assertThat(machineList.get(0).getQuantity(), is(12));
  }

  @Test
  public void testGetMachinesSqlException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, false);
    when(tableResultSet.getString(anyString())).thenThrow(SQLException.class);

    List<Machine> machineList = machineHandler.getMachines(BRANCH);
    assertThat(machineList, hasSize(0));
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

  private Machine getMockMachine() {
    return new MachineImpl(UUID.randomUUID().toString(), "aMachine", "12345", 2, "branch");
  }
}
