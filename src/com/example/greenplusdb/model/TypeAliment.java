package com.example.greenplusdb.model;

public enum TypeAliment {
    VIANDE(5.0),
    LEGUME(0.5);

    private final double impact;


    TypeAliment(double impact) {
        this.impact = impact;
    }

    public double getImpact() {
        return impact;
    }

}
