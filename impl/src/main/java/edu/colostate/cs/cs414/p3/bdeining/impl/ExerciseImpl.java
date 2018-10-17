package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Exercise;
import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import java.util.UUID;

public class ExerciseImpl implements Exercise {

  private String commonName;

  private transient String id;

  private Machine machine;

  private int sets;

  private int durationPerSet;

  public ExerciseImpl(String id, String commonName, Machine machine, int sets, int durationPerSet) {
    this.commonName = commonName;
    this.machine = machine;
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
  public Machine getMachine() {
    return machine;
  }

  @Override
  public void setMachine(Machine machine) {
    this.machine = machine;
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
}
