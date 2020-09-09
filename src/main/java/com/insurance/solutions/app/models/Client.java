package com.insurance.solutions.app.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "DNI can not be blank")
    private String dni;

    @NotBlank(message = "First name can not be blank")
    private String firstName;

    @NotBlank(message = "Last name can not be blank")
    private String lastName;

    @NotBlank(message = "Phone number can not be blank")
    private String phoneNumber;

    @NotBlank(message = "Mail can not be blank")
    private String mail;

    @NotBlank(message = "Insurance company can not be blank")
    private String insuranceCompany;

    @OneToMany(mappedBy = "client",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private final Set<Vehicle> vehicles = new HashSet<>();


    public Client() {
    }

    public Client(String dni, String firstName, String lastName, String phoneNumber, String mail, String insuranceCompany) {
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.insuranceCompany = insuranceCompany;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }
}
