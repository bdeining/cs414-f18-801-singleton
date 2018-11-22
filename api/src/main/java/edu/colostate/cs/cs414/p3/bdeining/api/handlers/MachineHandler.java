package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import java.sql.SQLException;
import java.util.List;

public interface MachineHandler {

  /**
   * Adds a {@link Machine} to the data store
   *
   * @param machine the machine to add
   * @return true when the addition was successful
   * @throws SQLException when a database error occurs
   */
  boolean addMachine(Machine machine) throws SQLException;

  /**
   * Removes a {@link Machine} from the data store
   *
   * @param id the id of the machine to remove
   * @return true when the removal was successful
   * @throws SQLException when a database error occurs
   */
  boolean removeMachine(String id) throws SQLException;

  /**
   * Gets a list of a all {@link Machine}s in the data store
   *
   * @return a list of machines
   * @throws SQLException when a database error occurs
   */
  List<Machine> getMachines() throws SQLException;

}
