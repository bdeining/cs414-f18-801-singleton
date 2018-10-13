package com.bdeining.api;

import java.util.List;

public interface Customer extends Person {

    List<WorkoutRoutine> getWorkoutRoutines();

    Activity getActivity();

}
