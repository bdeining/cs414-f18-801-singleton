package edu.colostate.cs.cs414.p3.bdeining.api;

public interface Exercise {

  String getId();

  String getCommonName();

  void setCommonName(String commonName);

  String getMachineId();

  void setMachineId(String machineId);

  int getSets();

  void setSets(int sets);

  int getDurationPerSet();

  void setDurationPerSet(int durationPerSet);
}
