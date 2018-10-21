package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import java.util.UUID;

public class ExerciseImpl implements Exercise {

  private String commonName;

  private transient String id;

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

  @Override
  public String getId() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
    return id;
  }

  @Override
  public String getCommonName() {
    return commonName;
  }

  @Override
  public void setCommonName(String commonName) {
    this.commonName = commonName;
  }

  @Override
  public String getMachineId() {
    return machineId;
  }

  @Override
  public void setMachineId(String machineId) {
    this.machineId = machineId;
  }

  @Override
  public int getSets() {
    return sets;
  }

  @Override
  public void setSets(int sets) {
    this.sets = sets;
  }

  @Override
  public int getDurationPerSet() {
    return durationPerSet;
  }

  @Override
  public void setDurationPerSet(int durationPerSet) {
    this.durationPerSet = durationPerSet;
  }

  @Override
  public String toString() {
    return String.format(
        "Name : %s, Machine ID : %s, Set : %s, Duration %s",
        commonName, machineId, sets, durationPerSet);
  }
}
