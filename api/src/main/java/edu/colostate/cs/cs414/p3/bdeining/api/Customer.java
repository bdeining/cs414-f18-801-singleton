package edu.colostate.cs.cs414.p3.bdeining.api;

import java.util.List;

public interface Customer extends Person {

  List<WorkoutRoutine> getWorkoutRoutines();

  Activity getActivity();
}
