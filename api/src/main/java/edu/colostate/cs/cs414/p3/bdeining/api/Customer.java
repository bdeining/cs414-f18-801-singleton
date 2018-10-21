package edu.colostate.cs.cs414.p3.bdeining.api;

import java.util.List;

public interface Customer extends Person {

  List<String> getWorkoutRoutineIds();

  Activity getActivity();

  void setWorkoutRoutineIds(List<String> workoutRoutineIds);

  void setActivity(Activity activity);
}
