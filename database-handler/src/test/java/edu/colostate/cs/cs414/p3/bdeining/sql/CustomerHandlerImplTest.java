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
import edu.colostate.cs.cs414.p3.bdeining.impl.CustomerImpl;
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

public class CustomerHandlerImplTest {

  private CustomerHandlerImpl customerHandler;

  private DataSource dataSource;

  private DatabaseMetaData databaseMetaData;

  private Connection connection;

  private Statement statement;

  private PreparedStatement preparedStatement;

  private CallableStatement callableStatement;

  @Before
  public void setUp() throws Exception {
    setUpMocks();
    customerHandler = new CustomerHandlerImpl();
    customerHandler.setDataSource(dataSource);
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

    customerHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(3)).execute(anyString());
  }

  @Test
  public void testInitGetMetadataSqlException() throws Exception {
    when(connection.getMetaData()).thenThrow(SQLException.class);
    customerHandler.init();

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
    customerHandler.init();

    verify(connection, times(2)).getMetaData();
    verify(databaseMetaData, times(2)).getTables(anyString(), anyString(), anyString(), any());
    verify(statement, times(3)).execute(anyString());
  }

  @Test
  public void testAddCustomer() throws Exception {
    boolean result = customerHandler.addCustomer(getMockCustomer());
    verify(preparedStatement, times(2)).execute();
    assertThat(result, is(true));
  }

  @Test
  public void testRemoveCustomer() throws Exception {
    boolean result = customerHandler.removeCustomer("anId");
    assertThat(result, is(true));
    verify(statement, times(4)).execute(anyString());
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

    List<Customer> customerList = customerHandler.getCustomers();
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
  public void testGetCustomersSQLException() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(statement.executeQuery(anyString())).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("id")).thenThrow(SQLException.class);

    when(callableStatement.executeQuery()).thenReturn(tableResultSet);

    List<Customer> customerList = customerHandler.getCustomers();
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
}
