package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import java.util.UUID;

public class MachineImpl implements Machine {

  private transient String id;

  private String name;

  private String picture;

  private int quantity;

  public MachineImpl(String id, String name, String picture, int quantity) {
    this.id = id;
    this.name = name;
    this.picture = picture;
    this.quantity = quantity;
  }

  @Override
  public String getId() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }

    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getPicture() {
    return picture;
  }

  @Override
  public void setPicture(String picture) {
    this.picture = picture;
  }

  @Override
  public int quantitiy() {
    return quantity;
  }

  @Override
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return String.format("Name : %s, ID : %s, Quantity : %d", name, id, quantity);
  }
}
