package com.insurance.solutions.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.insurance.solutions.app.models.enums.UserType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Username can not be blank")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Email can not be blank")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password can not be blank")
    private String password;

    private UserType type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "insurance_company_id")
    private InsuranceCompany insuranceCompany;

    public User() {
    }

    public User(String username, String email, String password, UserType type) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public InsuranceCompany getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }
}
