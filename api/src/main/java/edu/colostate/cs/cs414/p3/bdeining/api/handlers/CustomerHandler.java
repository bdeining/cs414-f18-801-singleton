package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import java.sql.SQLException;
import java.util.List;

import edu.colostate.cs.cs414.p3.bdeining.api.Customer;

public interface CustomerHandler {

  boolean addCustomer(Customer customer) throws SQLException;

  boolean removeCustomer(String id) throws SQLException;

  List<Customer> getCustomers() throws SQLException;
}
