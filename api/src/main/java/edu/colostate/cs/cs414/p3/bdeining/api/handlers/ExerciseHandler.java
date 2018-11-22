package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import java.sql.SQLException;
import java.util.List;

public interface ExerciseHandler {

  /**
   * Adds an {@link Exercise} to the data store
   *
   * @param exercise the exercise to add
   * @return true when the addition was successful
   * @throws SQLException when a database error occurs
   */
  boolean addExercise(Exercise exercise) throws SQLException;

  /**
   * Removes an {@link Exercise} from the data store
   *
   * @param id the id of the exercise to remove
   * @return true when the removal was successful
   * @throws SQLException when a database error occurs
   */
  boolean removeExercise(String id) throws SQLException;

  /**
   * Gets a list of a all {@link Exercise}s in the data store
   *
   * @return a list of exercises
   * @throws SQLException when a database error occurs
   */
  List<Exercise> getExercises() throws SQLException;
}
