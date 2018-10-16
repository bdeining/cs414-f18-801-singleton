package edu.colostate.cs.cs414.p3.bdeining.api;

import java.sql.SQLException;
import java.util.List;

public interface MySqlHandler {

  boolean addCustomer(Customer customer);

  boolean addExercise(Exercise exercise);

  boolean addMachine(Machine machine);

  boolean addTrainer(Trainer trainer) throws SQLException;

  boolean removeTrainer(String id) throws SQLException;

  List<Trainer> getTrainers() throws SQLException;

  boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine);
}
