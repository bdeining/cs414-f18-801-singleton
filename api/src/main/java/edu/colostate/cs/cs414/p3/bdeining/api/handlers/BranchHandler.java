package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import java.sql.SQLException;
import java.util.List;

public interface BranchHandler {

  /**
   * Adds a branch to the data store
   *
   * @param branch the branch to add
   * @return true when the addition was successful
   * @throws SQLException when a database error occurs
   */
  boolean addBranch(String branch) throws SQLException;

  /**
   * Removes a branch from the data store
   *
   * @param branch the id of the branch to remove
   * @return true when the removal was successful
   * @throws SQLException when a database error occurs
   */
  boolean removeBranch(String branch) throws SQLException;

  /**
   * Gets a list of a all branches in the data store
   *
   * @return a list of branches
   * @throws SQLException when a database error occurs
   */
  List<String> getBranches() throws SQLException;
}
