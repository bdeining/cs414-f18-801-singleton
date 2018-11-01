package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import java.sql.SQLException;
import java.util.List;

import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;

public interface WorkoutRoutineHandler {

  boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) throws SQLException;

  List<WorkoutRoutine> getWorkoutRoutines() throws SQLException;

  boolean removeWorkoutRoutine(String id) throws SQLException;
}
