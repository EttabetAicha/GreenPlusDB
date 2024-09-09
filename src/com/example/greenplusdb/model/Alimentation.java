package com.example.greenplusdb.model;

import com.example.greenplusdb.model.enums.TypeAliment;
import com.example.greenplusdb.model.enums.TypeConsommation;

public class Alimentation extends Consommation {
    private TypeAliment typeAliment;
    private double poids;


    public Alimentation(User user, TypeAliment typeAliment, double poids) {

        super(user, TypeConsommation.ALIMENTATION, calculateAlimentationImpact(poids, typeAliment));
        this.typeAliment = typeAliment;
        this.poids = poids;
    }


    public TypeAliment getTypeAliment() {
        return typeAliment;
    }

    public void setTypeAliment(TypeAliment typeAliment) {
        this.typeAliment = typeAliment;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }


    @Override
    public double calculerImpact() {
        return calculateAlimentationImpact(poids, typeAliment);
    }


    private static double calculateAlimentationImpact(double poids, TypeAliment typeAliment) {
        switch (typeAliment) {
            case VIANDE:
                return poids * 5.0;
            case LEGUME:
                return poids * 0.5;
            default:
                throw new IllegalArgumentException("Unknown food type: " + typeAliment);
        }
    }
}
