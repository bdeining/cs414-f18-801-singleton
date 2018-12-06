package edu.colostate.cs.cs414.p3.bdeining.sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;

public class BranchHandlerImplTest {

  private static final String BRANCH = "branch";

  private DataSource dataSource;

  private DatabaseMetaData databaseMetaData;

  private Connection connection;

  private Statement statement;

  private PreparedStatement preparedStatement;

  private CallableStatement callableStatement;

  private BranchHandlerImpl branchHandler;

  @Before
  public void setUp() throws Exception {
    setUpMocks();
    branchHandler = new BranchHandlerImpl();
    branchHandler.setDataSource(dataSource);
  }

  @Test
  public void testAddBranch() throws Exception {
    branchHandler.addBranch(BRANCH);
    verify(preparedStatement, times(4)).execute();
  }

  @Test(expected = SQLException.class)
  public void testAddBranchSqlException() throws Exception {
    when(preparedStatement.execute()).thenThrow(SQLException.class);
    branchHandler.addBranch(BRANCH);
  }

  @Test
  public void testRemoveBranch() throws Exception {
    branchHandler.removeBranch(BRANCH);
    verify(preparedStatement, times(4)).execute();
  }

  @Test
  public void testGetBranches() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(tableResultSet);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("name")).thenReturn("branch");

    List<String> branches = branchHandler.getBranches();
    verify(preparedStatement, times(3)).execute();
    verify(preparedStatement, times(1)).executeQuery();
    assertThat(branches, hasSize(2));
    assertThat(branches, hasItem(BRANCH));
  }

  @Test
  public void testGetBranchesNullResponse() throws Exception {
    ResultSet tableResultSet = mock(ResultSet.class);
    when(preparedStatement.executeQuery()).thenReturn(null);
    when(tableResultSet.next()).thenReturn(true, true, false);
    when(tableResultSet.getString("name")).thenReturn("branch");

    List<String> branches = branchHandler.getBranches();
    verify(preparedStatement, times(3)).execute();
    verify(preparedStatement, times(1)).executeQuery();
    assertThat(branches, hasSize(0));
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
