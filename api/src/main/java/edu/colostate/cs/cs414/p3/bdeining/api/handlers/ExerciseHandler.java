package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import java.sql.SQLException;
import java.util.List;

public interface ExerciseHandler {

  boolean addExercise(Exercise exercise) throws SQLException;

  List<Exercise> getExercises() throws SQLException;

  boolean removeExercise(String id) throws SQLException;
}
