package com.example.greenplusdb.model.enums;

public enum TypeVehicule {
    VOITURE(0.5),
    TRAIN(0.1);

    private final double impact;

    TypeVehicule(double impact) {
        this.impact = impact;
    }

    public double getImpact() {
        return impact;
    }
}
