package com.bdeining.impl;

import java.util.List;
import java.util.UUID;

import com.bdeining.api.Trainer;

//TODO Hashcode
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

    public TrainerImpl(String address, String firstName, String lastName, String phone, String email, String healthInsuranceProvider, int workHours, List<String> qualifications) {
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