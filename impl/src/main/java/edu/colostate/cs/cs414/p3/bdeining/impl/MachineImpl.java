package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Machine;
import java.util.UUID;

public class MachineImpl implements Machine {

  private String id;

  private String name;

  private String picture;

  private int quantity;

  public MachineImpl(String id, String name, String picture, int quantity) {
    this.id = id;
    this.name = name;
    this.picture = picture;
    this.quantity = quantity;
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
  public String getName() {
    return name;
  }

  /** {@inheritDoc} */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /** {@inheritDoc} */
  @Override
  public String getPicture() {
    return picture;
  }

  /** {@inheritDoc} */
  @Override
  public void setPicture(String picture) {
    this.picture = picture;
  }

  /** {@inheritDoc} */
  @Override
  public int getQuantity() {
    return quantity;
  }

  /** {@inheritDoc} */
  @Override
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format("Name : %s, ID : %s, Quantity : %d", name, id, quantity);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MachineImpl)) {
      return false;
    }

    MachineImpl machine = (MachineImpl) obj;

    return machine.getId().equals(getId());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + id.hashCode();
    hash = 31 * hash + name.hashCode();
    hash = 31 * hash + picture.hashCode();
    hash = 31 * hash + quantity;
    return hash;
  }
}
