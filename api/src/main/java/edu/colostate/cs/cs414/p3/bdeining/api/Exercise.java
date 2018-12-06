package edu.colostate.cs.cs414.p3.bdeining.api;

public interface Exercise {

  /**
   * Get the Exercise UUID.
   *
   * @return String representation of a UUID
   */
  String getId();

  /**
   * Get the Common Name of the Exercise
   *
   * @return String representation of the common name
   */
  String getCommonName();

  /**
   * Set the common name for the exercise
   *
   * @param commonName
   */
  void setCommonName(String commonName);

  /**
   * Get the {@link Machine}s UUID associated with the exercise
   *
   * @return String representation of a UUID
   */
  String getMachineId();

  /**
   * Set the {@link Machine}s UUID on the exercise
   *
   * @param machineId
   */
  void setMachineId(String machineId);

  /**
   * Get the number of sets for the exercise
   *
   * @return integer representation of number of sets
   */
  int getSets();

  /**
   * Set the number of sets for the exercise
   *
   * @param sets
   */
  void setSets(int sets);

  /**
   * Get the duration per set for the exercise represented in minutes.
   *
   * @return integer representation in minutes
   */
  int getDurationPerSet();

  /**
   * Set the duration for each set for the exercise
   *
   * @param durationPerSet integer duration in minutes
   */
  void setDurationPerSet(int durationPerSet);

  /**
   * Sets the Branch
   *
   * @param branch
   */
  void setBranch(String branch);

  /**
   * Gets the Branch
   *
   * @return
   */
  String getBranch();
}
