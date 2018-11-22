package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import java.sql.SQLException;
import java.util.List;

public interface TrainerHandler {

  /**
   * Adds a {@link Trainer} to the data store
   *
   * @param trainer the trainer to add
   * @return true when the addition was successful
   * @throws SQLException when a database error occurs
   */
  boolean addTrainer(Trainer trainer) throws SQLException;

  /**
   * Removes a {@link Trainer} from the data store
   *
   * @param id the id of the trainer to remove
   * @return true when the removal was successful
   * @throws SQLException when a database error occurs
   */
  boolean removeTrainer(String id) throws SQLException;

  /**
   * Gets a list of a all {@link Trainer}s in the data store
   *
   * @return a list of trainers
   * @throws SQLException when a database error occurs
   */
  List<Trainer> getTrainers() throws SQLException;
}
