package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import java.sql.SQLException;
import java.util.List;

public interface WorkoutRoutineHandler {

  /**
   * Adds a {@link WorkoutRoutine} to the data store
   *
   * @param workoutRoutine the workout routine to add
   * @return true when the addition was successful
   * @throws SQLException when a database error occurs
   */
  boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) throws SQLException;

  /**
   * Removes a {@link WorkoutRoutine} from the data store
   *
   * @param id the id of the workout routine to remove
   * @return true when the removal was successful
   * @throws SQLException when a database error occurs
   */
  boolean removeWorkoutRoutine(String id) throws SQLException;

  /**
   * Gets a list of a all {@link WorkoutRoutine}s in the data store
   *
   * @param branch the branch to get routine from
   * @return a list of workout routines
   * @throws SQLException when a database error occurs
   */
  List<WorkoutRoutine> getWorkoutRoutines(String branch) throws SQLException;
}
