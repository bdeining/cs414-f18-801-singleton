package edu.colostate.cs.cs414.p3.bdeining.api;

import java.sql.SQLException;
import java.util.List;

public interface MySqlHandler {

  boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) throws SQLException;

  List<WorkoutRoutine> getWorkoutRoutines() throws SQLException;

  boolean removeWorkoutRoutine(String id) throws SQLException;

  boolean addExercise(Exercise exercise) throws SQLException;

  List<Exercise> getExercies() throws SQLException;

  boolean removeExercise(String id) throws SQLException;

  boolean addMachine(Machine machine) throws SQLException;

  List<Machine> getMachines() throws SQLException;

  boolean removeMachine(String id) throws SQLException;

  boolean addTrainer(Trainer trainer) throws SQLException;

  boolean removeTrainer(String id) throws SQLException;

  List<Trainer> getTrainers() throws SQLException;

  boolean addCustomer(Customer customer) throws SQLException;

  boolean removeCustomer(String id) throws SQLException;

  List<Customer> getCustomers() throws SQLException;
}
