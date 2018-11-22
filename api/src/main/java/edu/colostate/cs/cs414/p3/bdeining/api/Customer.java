package edu.colostate.cs.cs414.p3.bdeining.api;

import java.util.List;

public interface Customer extends Person {

  /**
   * Get all workout routines that are assigned to the customer
   *
   * @return a list of workout routine ids
   */
  List<String> getWorkoutRoutineIds();

  /**
   * Get the activity status of the customer
   *
   * @return
   */
  Activity getActivity();

  /**
   * Set the list of workout routines for the customer
   *
   * @param workoutRoutineIds a list of workout routines
   */
  void setWorkoutRoutineIds(List<String> workoutRoutineIds);

  /**
   * Sets the activity status of the customer
   *
   * @param activity
   */
  void setActivity(Activity activity);
}
