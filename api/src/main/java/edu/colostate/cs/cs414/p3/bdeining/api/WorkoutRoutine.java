package edu.colostate.cs.cs414.p3.bdeining.api;

import java.util.List;

public interface WorkoutRoutine {

  /**
   * Gets the UUID for this workout routine
   *
   * @return
   */
  String getId();

  /**
   * Sets the name for this workout routine
   *
   * @param name
   */
  void setName(String name);

  /**
   * Gets the name of this workout routine
   *
   * @return
   */
  String getName();

  /**
   * Sets a list of UUIDs for exercises that are performed in this workout routine
   *
   * @param exerciseIds a list of exercise UUIDs
   */
  void setExcerciseIds(List<String> exerciseIds);

  /** Gets a list of UUIDs for exercises that are performed in this workout routine */
  List<String> getExerciseIds();
}
