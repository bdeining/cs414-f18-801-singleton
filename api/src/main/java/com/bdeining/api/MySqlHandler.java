package com.bdeining.api;

import java.util.List;

public interface MySqlHandler {

    boolean addCustomer(Customer customer);

    boolean addExercise(Exercise exercise);

    boolean addMachine(Machine machine);

    boolean addTrainer(Trainer trainer);

    boolean removeTrainer(String id);

    List<Trainer> getTrainers();

    boolean addWorkoutRoutine(WorkoutRoutine workoutRoutine);
}
