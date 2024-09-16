package com.example.greenplusdb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public User(Long id) {
        this.id = id;
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
        return Collections.unmodifiableList(consommations);
    }

    public void addConsommation(Consommation consommation) {
        consommations.add(consommation);
    }

    public void removeConsommation(Consommation consommation) {
        consommations.remove(consommation);
    }

    public double calculerConsommationTotale() {
        return consommations.stream()
                .mapToDouble(Consommation::calculerImpact)
                .sum();
    }

    public double calculerConsommationMoyenne(LocalDateTime startDate, LocalDateTime endDate) {
        return consommations.stream()
                .filter(c -> c.getStartDate() != null &&
                        c.getEndDate() != null &&
                        !c.getEndDate().isBefore(startDate) &&
                        !c.getStartDate().isAfter(endDate))
                .mapToDouble(Consommation::calculerImpact)
                .average()
                .orElse(0.0);
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
