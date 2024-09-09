package com.example.greenplusdb.model;

import com.example.greenplusdb.model.enums.TypeConsommation;
import com.example.greenplusdb.model.enums.TypeEnergie;

public class Logement extends Consommation {
    private TypeEnergie typeEnergie;
    private double consommationEnergie;


    public Logement(User user, TypeEnergie typeEnergie, double consommationEnergie) {
        // Calculate the impact before calling super()
        super(user, TypeConsommation.LOGEMENT, calculateLogementImpact(consommationEnergie, typeEnergie));
        this.typeEnergie = typeEnergie;
        this.consommationEnergie = consommationEnergie;
    }


    public TypeEnergie getTypeEnergie() {
        return typeEnergie;
    }

    public void setTypeEnergie(TypeEnergie typeEnergie) {
        this.typeEnergie = typeEnergie;
    }

    public double getConsommationEnergie() {
        return consommationEnergie;
    }

    public void setConsommationEnergie(double consommationEnergie) {
        this.consommationEnergie = consommationEnergie;
    }


    @Override
    public double calculerImpact() {
        return calculateLogementImpact(consommationEnergie, typeEnergie);
    }


    private static double calculateLogementImpact(double consommationEnergie, TypeEnergie typeEnergie) {
        switch (typeEnergie) {
            case ELECTRICITE:
                return consommationEnergie * 1.5;
            case GAZ:
                return consommationEnergie * 2.0;
            default:
                throw new IllegalArgumentException("Unknown energy type: " + typeEnergie);
        }
    }
}
