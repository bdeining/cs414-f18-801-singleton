package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import java.util.List;
import java.util.UUID;

public class TrainerImpl implements Trainer {

  private String address;

  private String firstName;

  private String lastName;

  private String phone;

  private String email;

  private String id;

  private String healthInsuranceProvider;

  private int workHours;

  private List<String> qualifications;

  private String password;

  public TrainerImpl(
      String id,
      String address,
      String firstName,
      String lastName,
      String phone,
      String email,
      String healthInsuranceProvider,
      int workHours,
      List<String> qualifications,
      String password) {
    this.address = address;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
    this.id = id;
    this.healthInsuranceProvider = healthInsuranceProvider;
    this.workHours = workHours;
    this.qualifications = qualifications;
    this.password = password;
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getPhone() {
    return phone;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getId() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
    return id;
  }

  @Override
  public String getHealthInsuranceProvider() {
    return healthInsuranceProvider;
  }

  @Override
  public int getWorkHours() {
    return workHours;
  }

  @Override
  public List<String> getQualifications() {
    return qualifications;
  }

  @Override
  public void setWorkHours(int workHours) {
    this.workHours = workHours;
  }

  @Override
  public void setQualifications(List<String> qualifications) {
    this.qualifications = qualifications;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void setHealthInsuranceProvider(String healthInsuranceProvider) {
    this.healthInsuranceProvider = healthInsuranceProvider;
  }

  @Override
  public String toString() {
    return String.format("Name : %s %s, Id : %s", firstName, lastName, id);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof TrainerImpl)) {
      return false;
    }

    TrainerImpl trainer = (TrainerImpl) obj;

    return trainer.getId().equals(getId());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + id.hashCode();
    hash = 31 * hash + firstName.hashCode();
    hash = 31 * hash + lastName.hashCode();
    hash = 31 * hash + phone.hashCode();
    hash = 31 * hash + email.hashCode();
    hash = 31 * hash + address.hashCode();
    hash = 31 * hash + healthInsuranceProvider.hashCode();
    hash = 31 * hash + workHours;
    hash = 31 * hash + qualifications.hashCode();
    return hash;
  }
}
