package com.example.greenplusdb.model;

import com.example.greenplusdb.model.enums.TypeConsommation;

import java.time.LocalDateTime;

public abstract class Consommation {

    private Long id;
    private User user;
    private TypeConsommation typeConsumption;
    private double impact;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public abstract double calculerImpact();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TypeConsommation getTypeConsumption() {
        return typeConsumption;
    }

    public void setTypeConsumption(TypeConsommation typeConsumption) {
        this.typeConsumption = typeConsumption;
    }

    public double getImpact() {
        return impact;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Consommation{" +
                "id=" + id +
                ", user=" + user +
                ", typeConsumption=" + typeConsumption +
                ", impact=" + impact +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
