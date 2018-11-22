package edu.colostate.cs.cs414.p3.bdeining.api;

public interface Person {

  /**
   * Gets the address for this person
   *
   * @return
   */
  String getAddress();

  /**
   * Gets the first name of this person
   *
   * @return
   */
  String getFirstName();

  /**
   * Gets the last name of this person
   *
   * @return
   */
  String getLastName();

  /**
   * Gets the phone number for this person represented as a String
   *
   * @return
   */
  String getPhone();

  /**
   * Gets the email of this person
   *
   * @return
   */
  String getEmail();

  /**
   * Gets the UUID for this person
   *
   * @return
   */
  String getId();

  /**
   * Gets the Health Insurance Provider of this person
   *
   * @return
   */
  String getHealthInsuranceProvider();

  /**
   * Sets the address of this person
   *
   * @param address
   */
  void setAddress(String address);

  /**
   * Sets the first name of this person
   *
   * @param firstName
   */
  void setFirstName(String firstName);

  /**
   * Sets the last name of this person
   *
   * @param lastName
   */
  void setLastName(String lastName);

  /**
   * Sets the phone number of this person
   *
   * @param phone
   */
  void setPhone(String phone);

  /**
   * Sets the email for this person
   *
   * @param email
   */
  void setEmail(String email);

  /**
   * Sets the Health Insurance Provider of this person
   *
   * @param healthInsuranceProvider
   */
  void setHealthInsuranceProvider(String healthInsuranceProvider);
}
