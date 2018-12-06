package edu.colostate.cs.cs414.p3.bdeining.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class MachineImplTest {

  private MachineImpl machine;

  private static final String ID = UUID.randomUUID().toString();

  private static final String NAME = "Routine2";

  private static final String PICTURE = "12315";

  private static final int QUANTITY = 12;

  private static final String BRANCH = "branch";

  @Before
  public void setUp() {
    machine = new MachineImpl(ID, NAME, PICTURE, QUANTITY, BRANCH);
  }

  @Test
  public void testGetters() {
    assertFields(ID);
  }

  @Test
  public void testSetters() {
    machine = new MachineImpl(null, null, null, 0, null);
    machine.setName(NAME);
    machine.setPicture(PICTURE);
    machine.setQuantity(QUANTITY);
    machine.setBranch(BRANCH);
    assertFields(null);
  }

  @Test
  public void testNullId() {
    machine = new MachineImpl(null, NAME, PICTURE, QUANTITY, BRANCH);
    assertThat(machine.getId(), notNullValue());
  }

  @Test
  public void testToString() {
    assertThat(machine.toString(), containsString(NAME));
  }

  @Test
  public void testEqual() {
    assertThat(machine, is(machine));
  }

  @Test
  public void testNotEqualOtherObject() {
    assertThat(machine, not("12"));
  }

  private void assertFields(String id) {
    if (id != null) {
      assertThat(machine.getId(), is(id));

    } else {
      assertThat(machine.getId(), notNullValue());
    }
    assertThat(machine.getPicture(), is(PICTURE));
    assertThat(machine.getQuantity(), is(QUANTITY));
    assertThat(machine.getBranch(), is(BRANCH));
    assertThat(machine.getName(), is(NAME));
  }

  @Test
  public void testHashCode() {
    assertThat(machine.hashCode(), not(0));
  }
}
