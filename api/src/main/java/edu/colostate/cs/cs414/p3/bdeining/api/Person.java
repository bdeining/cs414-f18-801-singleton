package edu.colostate.cs.cs414.p3.bdeining.api;

public interface Person {

  String getAddress();

  String getFirstName();

  String getLastName();

  String getPhone();

  String getEmail();

  String getId();

  String getHealthInsuranceProvider();

  void setAddress(String address);

  void setFirstName(String firstName);

  void setLastName(String lastName);

  void setPhone(String phone);

  void setEmail(String email);

  void setHealthInsuranceProvider(String healthInsuranceProvider);
}
