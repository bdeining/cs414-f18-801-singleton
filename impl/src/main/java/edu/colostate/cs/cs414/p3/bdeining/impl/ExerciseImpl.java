package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import java.util.UUID;

public class ExerciseImpl implements Exercise {

  private String commonName;

  private String id;

  private String machineId;

  private int sets;

  private int durationPerSet;

  public ExerciseImpl(
      String id, String commonName, String machineId, int sets, int durationPerSet) {
    this.commonName = commonName;
    this.machineId = machineId;
    this.sets = sets;
    this.id = id;
    this.durationPerSet = durationPerSet;
  }

  /** {@inheritDoc} */
  @Override
  public String getId() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
    return id;
  }

  /** {@inheritDoc} */
  @Override
  public String getCommonName() {
    return commonName;
  }

  /** {@inheritDoc} */
  @Override
  public void setCommonName(String commonName) {
    this.commonName = commonName;
  }

  /** {@inheritDoc} */
  @Override
  public String getMachineId() {
    return machineId;
  }

  /** {@inheritDoc} */
  @Override
  public void setMachineId(String machineId) {
    this.machineId = machineId;
  }

  /** {@inheritDoc} */
  @Override
  public int getSets() {
    return sets;
  }

  /** {@inheritDoc} */
  @Override
  public void setSets(int sets) {
    this.sets = sets;
  }

  /** {@inheritDoc} */
  @Override
  public int getDurationPerSet() {
    return durationPerSet;
  }

  /** {@inheritDoc} */
  @Override
  public void setDurationPerSet(int durationPerSet) {
    this.durationPerSet = durationPerSet;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format(
        "Name : %s, Machine ID : %s, Set : %s, Duration %s",
        commonName, machineId, sets, durationPerSet);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ExerciseImpl)) {
      return false;
    }

    ExerciseImpl exercise = (ExerciseImpl) obj;

    return exercise.getId().equals(getId());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + id.hashCode();
    hash = 31 * hash + commonName.hashCode();
    hash = 31 * hash + machineId.hashCode();
    hash = 31 * hash + sets;
    hash = 31 * hash + durationPerSet;
    return hash;
  }
}
