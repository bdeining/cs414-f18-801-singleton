package edu.colostate.cs.cs414.p3.bdeining.api;

import java.util.List;

public interface WorkoutRoutine {

  String getId();

  void setName(String name);

  String getName();

  void setExcercises(List<Exercise> exercises);

  List<Exercise> getExercises();
}
