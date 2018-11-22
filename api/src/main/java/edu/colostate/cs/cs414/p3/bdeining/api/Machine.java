package edu.colostate.cs.cs414.p3.bdeining.api;

public interface Machine {

  /**
   * Get the Machine UUID.
   *
   * @return String representation of a UUID
   */
  String getId();

  /**
   * Get the Machine's name
   *
   * @return
   */
  String getName();

  /**
   * Set the Machine's name
   *
   * @param name
   */
  void setName(String name);

  /**
   * Returns a Base64 encoded string of an image of the machine
   *
   * @return String representation of the picture
   */
  String getPicture();

  /**
   * Sets the picture of the machine
   *
   * @param base64 Base64 string representation of an image
   */
  void setPicture(String base64);

  /**
   * Get the number of this type of machine within the store
   *
   * @return
   */
  int getQuantity();

  /**
   * Set the number of machines within the store
   *
   * @param quantity integer amount of machines
   */
  void setQuantity(int quantity);
}
