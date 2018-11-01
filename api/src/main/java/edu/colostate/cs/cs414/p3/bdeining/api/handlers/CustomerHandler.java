package edu.colostate.cs.cs414.p3.bdeining.api.handlers;

import java.sql.SQLException;
import java.util.List;

import edu.colostate.cs.cs414.p3.bdeining.api.Customer;

public interface CustomerHandler {

  /*  boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine) throws SQLException;

  List<WorkoutRoutine> getWorkoutRoutines() throws SQLException;

  boolean removeWorkoutRoutine(String id) throws SQLException;*/

  /*  boolean addExercise(Exercise exercise) throws SQLException;

  List<Exercise> getExercises() throws SQLException;

  boolean removeExercise(String id) throws SQLException;*/

  /*
    boolean addMachine(Machine machine) throws SQLException;

    List<Machine> getMachines() throws SQLException;

    boolean removeMachine(String id) throws SQLException;
  */

  /*
    boolean addTrainer(Trainer trainer) throws SQLException;

    boolean removeTrainer(String id) throws SQLException;

    List<Trainer> getTrainers() throws SQLException;
  */

  boolean addCustomer(Customer customer) throws SQLException;

  boolean removeCustomer(String id) throws SQLException;

  List<Customer> getCustomers() throws SQLException;
}
