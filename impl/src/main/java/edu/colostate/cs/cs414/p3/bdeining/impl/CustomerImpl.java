package edu.colostate.cs.cs414.p3.bdeining.impl;

import edu.colostate.cs.cs414.p3.bdeining.api.Activity;
import edu.colostate.cs.cs414.p3.bdeining.api.Customer;
import edu.colostate.cs.cs414.p3.bdeining.api.WorkoutRoutine;
import java.util.List;
import java.util.UUID;

public class CustomerImpl implements Customer {

  private String address;

  private String firstName;

  private String lastName;

  private String phone;

  private String email;

  private String id;

  private String healthInsuranceProvider;

  private Activity activity;

  private List<WorkoutRoutine> workoutRoutines;

  public CustomerImpl(
      String address,
      String firstName,
      String lastName,
      String phone,
      String email,
      String healthInsuranceProvider,
      List<WorkoutRoutine> workoutRoutines,
      Activity activity) {
    this.address = address;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
    this.id = UUID.randomUUID().toString();
    this.healthInsuranceProvider = healthInsuranceProvider;
    this.activity = activity;
    this.workoutRoutines = workoutRoutines;
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
  public Activity getActivity() {
    return activity;
  }

  @Override
  public List<WorkoutRoutine> getWorkoutRoutines() {
    return workoutRoutines;
  }
}
