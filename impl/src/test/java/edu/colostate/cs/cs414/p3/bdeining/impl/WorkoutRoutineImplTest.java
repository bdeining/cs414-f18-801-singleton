package edu.colostate.cs.cs414.p3.bdeining.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class WorkoutRoutineImplTest {

  private WorkoutRoutineImpl workoutRoutine;

  private static final String ID = UUID.randomUUID().toString();

  private static final String NAME = "Routine2";

  private static final List<String> EXERCISE_LIST = Arrays.asList("ex1", "ex2");

  @Before
  public void setUp() {
    workoutRoutine = new WorkoutRoutineImpl(ID, NAME, EXERCISE_LIST);
  }

  @Test
  public void testGetters() {
    assertFields(ID);
  }

  @Test
  public void testSetters() {
    workoutRoutine = new WorkoutRoutineImpl(null, null, null);
    workoutRoutine.setName(NAME);
    workoutRoutine.setExcerciseIds(EXERCISE_LIST);
    assertFields(null);
  }

  @Test
  public void testNullId() {
    workoutRoutine = new WorkoutRoutineImpl(null, NAME, EXERCISE_LIST);
    assertThat(workoutRoutine.getId(), notNullValue());
  }

  @Test
  public void testToString() {
    assertThat(workoutRoutine.toString(), containsString(NAME));
  }

  @Test
  public void testEqual() {
    assertThat(workoutRoutine, is(workoutRoutine));
  }

  @Test
  public void testNotEqualOtherObject() {
    assertThat(workoutRoutine, not("12"));
  }

  private void assertFields(String id) {
    if (id != null) {
      assertThat(workoutRoutine.getId(), is(id));

    } else {
      assertThat(workoutRoutine.getId(), notNullValue());
    }
    assertThat(workoutRoutine.getExerciseIds(), is(EXERCISE_LIST));
    assertThat(workoutRoutine.getName(), is(NAME));
  }

  @Test
  public void testHashCode() {
    assertThat(workoutRoutine.hashCode(), not(0));
  }
}
