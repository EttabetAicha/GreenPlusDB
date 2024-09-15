package com.example.greenplusdb.model;

import com.example.greenplusdb.model.enums.TypeAliment;
import com.example.greenplusdb.model.enums.TypeConsommation;

public class Alimentation extends Consommation {
    private TypeAliment typeAliment;
    private double poids;

    @Override
    public double calculerImpact() {
        double impactPerKg;
        switch (typeAliment) {
            case VIANDE:
                impactPerKg = 5.0;
                break;
            case LEGUME:
                impactPerKg = 0.5;
                break;
            default:
                throw new IllegalArgumentException("Unknown type of aliment: " + typeAliment);
        }
        return impactPerKg * poids;
    }

    @Override
    public void setImpact(double impact) {
        super.setImpact(impact);
    }

    @Override
    public TypeConsommation getTypeConsumption() {

        return TypeConsommation.ALIMENTATION;
    }



    public TypeAliment getTypeAliment() { return typeAliment; }
    public void setTypeAliment(TypeAliment typeAliment) { this.typeAliment = typeAliment; }
    public double getPoids() { return poids; }
    public void setPoids(double poids) { this.poids = poids; }

    @Override
    public String toString() {
        return "Alimentation{" +
                "id=" + getId() +
                ", user=" + getUser() +
                ", typeConsumption=" + getTypeConsumption() +
                ", impact=" + getImpact() +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", typeAliment=" + typeAliment +
                ", poids=" + poids +
                '}';
    }
}
