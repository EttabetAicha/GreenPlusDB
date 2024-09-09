package com.example.greenplusdb.model.enums;

public enum TypeEnergie {
    ELECTRICITE(1.5),
    GAZ(2.0);

    private final double impact;

    TypeEnergie(double impact) {
        this.impact = impact;
    }

    public double getImpact() {
        return impact;
    }
}
