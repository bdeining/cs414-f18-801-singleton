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

public class TrainerImplTest {

  private TrainerImpl trainer;

  private static final String ID = UUID.randomUUID().toString();

  private static final String ADDRESS = "123 Fake St.";

  private static final String FIRST_NAME = "Bob";

  private static final String LAST_NAME = "Hope";

  private static final String PHONE = "123-456-7890";

  private static final String EMAIL = "excellent@exmaple.com";

  private static final String HEALTH_INSURANCE_PROVIDER = "Kaiser";

  private static final List<String> QUALIFICATIONS = Arrays.asList("qual1", "qual2");

  private static final String PASSWORD = "password";

  private static final String BRANCH = "branch1";

  private static final int WORK_HOURS = 12;

  @Before
  public void setUp() {
    trainer =
        new TrainerImpl(
            ID,
            ADDRESS,
            FIRST_NAME,
            LAST_NAME,
            PHONE,
            EMAIL,
            HEALTH_INSURANCE_PROVIDER,
            BRANCH,
            WORK_HOURS,
            QUALIFICATIONS,
            PASSWORD);
  }

  @Test
  public void testGetters() {
    assertFields(ID);
  }

  @Test
  public void testSetters() {
    trainer = new TrainerImpl(null, null, null, null, null, null, null, null, 0, null, null);
    trainer.setAddress(ADDRESS);
    trainer.setFirstName(FIRST_NAME);
    trainer.setLastName(LAST_NAME);
    trainer.setPhone(PHONE);
    trainer.setEmail(EMAIL);
    trainer.setHealthInsuranceProvider(HEALTH_INSURANCE_PROVIDER);
    trainer.setWorkHours(WORK_HOURS);
    trainer.setBranch(BRANCH);
    trainer.setQualifications(QUALIFICATIONS);
    trainer.setPassword(PASSWORD);
    assertFields(null);
  }

  @Test
  public void testNullId() {
    trainer =
        new TrainerImpl(
            null,
            ADDRESS,
            FIRST_NAME,
            LAST_NAME,
            PHONE,
            EMAIL,
            HEALTH_INSURANCE_PROVIDER,
            BRANCH,
            WORK_HOURS,
            QUALIFICATIONS,
            PASSWORD);
    assertThat(trainer.getId(), notNullValue());
  }

  @Test
  public void testToString() {
    assertThat(trainer.toString(), containsString(FIRST_NAME));
    assertThat(trainer.toString(), containsString(LAST_NAME));
  }

  @Test
  public void testEqual() {
    assertThat(trainer, is(trainer));
  }

  @Test
  public void testNotEqualOtherObject() {
    assertThat(trainer, not("12"));
  }

  private void assertFields(String id) {
    if (id != null) {
      assertThat(trainer.getId(), is(id));

    } else {
      assertThat(trainer.getId(), notNullValue());
    }
    assertThat(trainer.getAddress(), is(ADDRESS));
    assertThat(trainer.getFirstName(), is(FIRST_NAME));
    assertThat(trainer.getLastName(), is(LAST_NAME));
    assertThat(trainer.getPhone(), is(PHONE));
    assertThat(trainer.getEmail(), is(EMAIL));
    assertThat(trainer.getBranch(), is(BRANCH));
    assertThat(trainer.getHealthInsuranceProvider(), is(HEALTH_INSURANCE_PROVIDER));
    assertThat(trainer.getQualifications(), is(QUALIFICATIONS));
    assertThat(trainer.getWorkHours(), is(WORK_HOURS));
    assertThat(trainer.getPassword(), is(PASSWORD));
  }

  @Test
  public void testHashCode() {
    assertThat(trainer.hashCode(), not(0));
  }
}
