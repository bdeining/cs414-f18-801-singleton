package edu.colostate.cs.cs414.p3.bdeining.api;

import java.util.List;

public interface Trainer extends Person {

  /**
   * Get the number of hours this trainer works for a week
   *
   * @return integer representation of hours
   */
  int getWorkHours();

  /**
   * Gets the list of qualifications for this trainer
   *
   * @return list of qualifications represented as strings
   */
  List<String> getQualifications();

  /**
   * Sets the number of hours this trainer can work in a week
   *
   * @param workHours
   */
  void setWorkHours(int workHours);

  /**
   * Sets the list of qualifications for this trainer
   *
   * @param qualifications
   */
  void setQualifications(List<String> qualifications);

  /**
   * Gets the password for this trainer's account
   *
   * @return string representation of a password
   */
  String getPassword();

  /**
   * Sets the password for this trainer
   *
   * @param password
   */
  void setPassword(String password);
}
