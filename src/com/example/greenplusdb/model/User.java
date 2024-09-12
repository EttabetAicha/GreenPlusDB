package com.example.greenplusdb.model;

import java.util.*;

public class User {
    private Long id;
    private String name;
    private String email;
    private final List<Consommation> consommations = new ArrayList<>();
    private double totalImpact;

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

    public List<Consommation> getConsommations() {
        return consommations;
    }

    public void addConsommation(Consommation consommation) {
        consommations.add(consommation);
        updateTotalImpact();
    }

    public void removeConsommation(Consommation consommation) {
        consommations.remove(consommation);
        updateTotalImpact();
    }


    public double calculerConsommationTotale() {
        return consommations.stream().mapToDouble(Consommation::calculerImpact).sum();
    }


    private void updateTotalImpact() {
        this.totalImpact = calculerConsommationTotale();
    }

    public double getTotalImpact() {
        return totalImpact;
    }

    public void setTotalImpact(double totalImpact) {
        this.totalImpact = totalImpact;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", totalImpact=" + totalImpact +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
