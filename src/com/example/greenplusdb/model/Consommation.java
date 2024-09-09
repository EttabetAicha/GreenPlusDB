package com.example.greenplusdb.model;
import com.example.greenplusdb.model.enums.TypeConsommation;

import java.sql.Timestamp;
public abstract class Consommation{
    private int id;
    private User user;
    private TypeConsommation typeConsommation;
    private  double impact;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Consommation(User user, TypeConsommation typeConsommation, double impact) {
        this.user = user;
        this.typeConsommation = typeConsommation;
        this.impact = impact;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TypeConsommation getTypeConsommation() {
        return typeConsommation;
    }

    public void setTypeConsommation(TypeConsommation typeConsommation) {
        this.typeConsommation = typeConsommation;
    }

    public double getImpact() {
        return impact;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    public abstract double calculerImpact();
}
