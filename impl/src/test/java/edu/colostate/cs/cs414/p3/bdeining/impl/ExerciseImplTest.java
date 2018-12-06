package edu.colostate.cs.cs414.p3.bdeining.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class ExerciseImplTest {

  private ExerciseImpl exercise;

  private static final String ID = UUID.randomUUID().toString();

  private static final String COMMON_NAME = "Routine2";

  private static final String MACHINE_ID = "12315";

  private static final int SET = 12;

  private static final int DURATION = 2;

  private static final String BRANCH = "branch";

  @Before
  public void setUp() {
    exercise = new ExerciseImpl(ID, COMMON_NAME, MACHINE_ID, SET, DURATION, BRANCH);
  }

  @Test
  public void testGetters() {
    assertFields(ID);
  }

  @Test
  public void testSetters() {
    exercise = new ExerciseImpl(null, null, null, 0, 0, BRANCH);
    exercise.setCommonName(COMMON_NAME);
    exercise.setMachineId(MACHINE_ID);
    exercise.setSets(SET);
    exercise.setDurationPerSet(DURATION);
    exercise.setBranch(BRANCH);
    assertFields(null);
  }

  @Test
  public void testNullId() {
    exercise = new ExerciseImpl(null, COMMON_NAME, MACHINE_ID, SET, DURATION, BRANCH);
    assertThat(exercise.getId(), notNullValue());
  }

  @Test
  public void testToString() {
    assertThat(exercise.toString(), containsString(COMMON_NAME));
    assertThat(exercise.toString(), containsString(MACHINE_ID));
    assertThat(exercise.toString(), containsString("" + SET));
    assertThat(exercise.toString(), containsString("" + DURATION));
  }

  @Test
  public void testEqual() {
    assertThat(exercise, is(exercise));
  }

  @Test
  public void testNotEqualOtherObject() {
    assertThat(exercise, not("12"));
  }

  private void assertFields(String id) {
    if (id != null) {
      assertThat(exercise.getId(), is(id));

    } else {
      assertThat(exercise.getId(), notNullValue());
    }
    assertThat(exercise.getCommonName(), is(COMMON_NAME));
    assertThat(exercise.getDurationPerSet(), is(DURATION));
    assertThat(exercise.getSets(), is(SET));
    assertThat(exercise.getBranch(), is(BRANCH));
    assertThat(exercise.getMachineId(), is(MACHINE_ID));
  }

  @Test
  public void testHashCode() {
    assertThat(exercise.hashCode(), not(0));
  }
}
