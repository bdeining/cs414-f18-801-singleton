package edu.colostate.cs.cs414.p3.bdeining.api;

public interface Machine {

  String getId();

  String getName();

  void setName(String name);

  String getPicture();

  void setPicture(String bytes);

  int getQuantity();

  void setQuantity(int quantity);
}
