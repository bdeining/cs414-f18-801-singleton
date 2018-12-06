package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import java.sql.SQLException;
import java.util.List;

public interface CustomerHandler {

  /**
   * Add a {@link Customer} to the a data store
   *
   * @param customer the customer to add
   * @return true if the addition was successful
   * @throws SQLException when a database error occurs
   */
  boolean addCustomer(Customer customer) throws SQLException;

  /**
   * Remove a {@link Customer} from the data store
   *
   * @param id the id of the customer to remove
   * @return true if the removal was successful
   * @throws SQLException when a database error occurs
   */
  boolean removeCustomer(String id) throws SQLException;

  /**
   * Gets a list of all {@link Customer}s that are in the data store
   *
   * @param branch the branch to get customer from
   * @return a list of customers
   * @throws SQLException when a database error occurs
   */
  List<Customer> getCustomers(String branch) throws SQLException;
}
