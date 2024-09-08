package com.example.greenplusdb.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    private Long id;
    private String name;
    private String email;
    private final Set<Consommation> consommations = new HashSet<>();


    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Consommation> getConsommations() {
        return consommations;
    }

    public void addConsommation(Consommation consommation) {
        consommations.add(consommation);
    }

    public void removeConsommation(Consommation consommation) {
        consommations.remove(consommation);
    }

    public double calculerConsommationTotale() {
        return consommations.stream().mapToDouble(Consommation::calculerImpact).sum();
    }
}

