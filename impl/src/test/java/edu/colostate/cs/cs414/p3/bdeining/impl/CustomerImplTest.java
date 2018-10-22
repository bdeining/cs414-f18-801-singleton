package edu.colostate.cs.cs414.p3.bdeining.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import edu.colostate.cs.cs414.p3.bdeining.api.Activity;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;

public class CustomerImplTest {

  private CustomerImpl customer;

  private static final String ID = UUID.randomUUID().toString();

  private static final String ADDRESS = "123 Fake St.";

  private static final String FIRST_NAME = "Bob";

  private static final String LAST_NAME = "Hope";

  private static final String PHONE = "123-456-7890";

  private static final String EMAIL = "excellent@exmaple.com";

  private static final String HEALTH_INSURANCE_PROVIDER = "Kaiser";

  private static final List<String> WORKOUT_ROUTINE = Arrays.asList("id1", "id2");

  private static final Activity ACTIVITY = Activity.ACTIVE;

  @Before
  public void setUp() {
    customer =
        new CustomerImpl(
            ID,
            ADDRESS,
            FIRST_NAME,
            LAST_NAME,
            PHONE,
            EMAIL,
            HEALTH_INSURANCE_PROVIDER,
            WORKOUT_ROUTINE,
            ACTIVITY);
  }

  @Test
  public void testGetters() {
    assertFields(ID);
  }

  @Test
  public void testSetters() {
    customer = new CustomerImpl(null, null, null, null, null, null, null, null, null);
    customer.setAddress(ADDRESS);
    customer.setFirstName(FIRST_NAME);
    customer.setLastName(LAST_NAME);
    customer.setPhone(PHONE);
    customer.setEmail(EMAIL);
    customer.setHealthInsuranceProvider(HEALTH_INSURANCE_PROVIDER);
    customer.setWorkoutRoutineIds(WORKOUT_ROUTINE);
    customer.setActivity(ACTIVITY);
    assertFields(null);
  }

  @Test
  public void testNullId() {
    customer =
        new CustomerImpl(
            null,
            ADDRESS,
            FIRST_NAME,
            LAST_NAME,
            PHONE,
            EMAIL,
            HEALTH_INSURANCE_PROVIDER,
            WORKOUT_ROUTINE,
            ACTIVITY);
    assertThat(customer.getId(), notNullValue());
  }

  @Test
  public void testToString() {
    assertThat(customer.toString(), containsString(FIRST_NAME));
    assertThat(customer.toString(), containsString(LAST_NAME));
  }

  @Test
  public void testEqual() {
    assertThat(customer, is(customer));
  }

  @Test
  public void testNotEqualOtherObject() {
    assertThat(customer, not("12"));
  }

  private void assertFields(String id) {
    if (id != null) {
      assertThat(customer.getId(), is(id));

    } else {
      assertThat(customer.getId(), notNullValue());
    }
    assertThat(customer.getAddress(), is(ADDRESS));
    assertThat(customer.getFirstName(), is(FIRST_NAME));
    assertThat(customer.getLastName(), is(LAST_NAME));
    assertThat(customer.getPhone(), is(PHONE));
    assertThat(customer.getEmail(), is(EMAIL));
    assertThat(customer.getHealthInsuranceProvider(), is(HEALTH_INSURANCE_PROVIDER));
    assertThat(customer.getWorkoutRoutineIds(), is(WORKOUT_ROUTINE));
    assertThat(customer.getActivity(), is(ACTIVITY));
  }

  @Test
  public void testHashCode() {
    assertThat(customer.hashCode(), not(0));
  }
}
