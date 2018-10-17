package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import java.util.List;
import java.util.UUID;

public class WorkoutRoutineImpl implements edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine {

  private transient String id;

  private String name;

  private List<Exercise> exercises;

  public WorkoutRoutineImpl(String id, String name, List<Exercise> exercises) {
    this.id = id;
    this.name = name;
    this.exercises = exercises;
  }

  @Override
  public String getId() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
    return id;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setExcercises(List<Exercise> exercises) {
    this.exercises = exercises;
  }

  @Override
  public List<Exercise> getExercises() {
    return exercises;
  }
}
