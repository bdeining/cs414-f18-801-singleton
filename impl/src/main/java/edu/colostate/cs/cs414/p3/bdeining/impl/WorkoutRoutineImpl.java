package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import java.util.List;
import java.util.UUID;

public class WorkoutRoutineImpl implements WorkoutRoutine {

  private String id;

  private String name;

  private List<String> exerciseIds;

  public WorkoutRoutineImpl(String id, String name, List<String> exerciseIds) {
    this.id = id;
    this.name = name;
    this.exerciseIds = exerciseIds;
  }

  /** {@inheritDoc} */
  @Override
  public String getId() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
    return id;
  }

  /** {@inheritDoc} */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return name;
  }

  /** {@inheritDoc} */
  @Override
  public void setExcerciseIds(List<String> exerciseIds) {
    this.exerciseIds = exerciseIds;
  }

  /** {@inheritDoc} */
  @Override
  public List<String> getExerciseIds() {
    return exerciseIds;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("Name : %s, ID : %s", name, id);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof WorkoutRoutineImpl)) {
      return false;
    }

    WorkoutRoutineImpl workoutRoutine = (WorkoutRoutineImpl) obj;

    return workoutRoutine.getId().equals(getId());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + id.hashCode();
    hash = 31 * hash + name.hashCode();
    hash = 31 * hash + exerciseIds.hashCode();
    return hash;
  }
}
