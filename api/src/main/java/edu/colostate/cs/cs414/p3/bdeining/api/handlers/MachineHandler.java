package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import java.sql.SQLException;
import java.util.List;

public interface MachineHandler {

  boolean addMachine(Machine machine) throws SQLException;

  List<Machine> getMachines() throws SQLException;

  boolean removeMachine(String id) throws SQLException;
}
