package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Trainer;
import java.util.List;
import java.util.UUID;

// TODO Hashcode
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

  public TrainerImpl(
      String address,
      String firstName,
      String lastName,
      String phone,
      String email,
      String healthInsuranceProvider,
      int workHours,
      List<String> qualifications) {
    this.address = address;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
    this.id = UUID.randomUUID().toString();
    this.healthInsuranceProvider = healthInsuranceProvider;
    this.workHours = workHours;
    this.qualifications = qualifications;
  }

  public TrainerImpl(
      String id,
      String address,
      String firstName,
      String lastName,
      String phone,
      String email,
      String healthInsuranceProvider,
      int workHours,
      List<String> qualifications) {
    this.address = address;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
    this.id = id;
    this.healthInsuranceProvider = healthInsuranceProvider;
    this.workHours = workHours;
    this.qualifications = qualifications;
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
  public String toString() {
    return String.format("Name : %s %s, Id : %s", firstName, lastName, id);
  }
}
