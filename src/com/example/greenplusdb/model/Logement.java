package com.example.greenplusdb.model;

import com.example.greenplusdb.model.enums.TypeEnergie;

public class Logement extends Consommation {

    private double consommationEnergie;
    private TypeEnergie typeEnergie;

    @Override
    public double calculerImpact() {
        double impactPerUnit = 0.0;
        switch (typeEnergie) {
            case ELECTRICITE:
                impactPerUnit = 1.5;
                break;
            case GAZ:
                impactPerUnit = 2.0;
                break;
        }
        return impactPerUnit * consommationEnergie;
    }
    @Override
    public void setImpact(double impact) {
        super.setImpact(impact);
    }


    public double getConsommationEnergie() { return consommationEnergie; }
    public void setConsommationEnergie(double consommationEnergie) { this.consommationEnergie = consommationEnergie; }
    public TypeEnergie getTypeEnergie() { return typeEnergie; }
    public void setTypeEnergie(TypeEnergie typeEnergie) { this.typeEnergie = typeEnergie; }
    @Override
    public String toString() {
        return "Logement{" +

                ", user=" + getUser() +
                ", typeConsumption=" + getTypeConsumption() +
                ", impact=" + getImpact() +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                "consommationEnergie=" + consommationEnergie +
                ", typeEnergie=" + typeEnergie +
                ", " + super.toString() +
                '}';
    }
}
