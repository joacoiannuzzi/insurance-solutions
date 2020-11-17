package com.insurance.solutions.app.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class InsuranceCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name can not be blank")
    private String name;

    @OneToMany(mappedBy = "insuranceCompany",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "insuranceCompany",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    public InsuranceCompany() {
    }

    public InsuranceCompany(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
