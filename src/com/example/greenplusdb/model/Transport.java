package com.example.greenplusdb.model;

import com.example.greenplusdb.model.enums.TypeConsommation;
import com.example.greenplusdb.model.enums.TypeVehicule;

public class Transport extends Consommation {

    private double distanceParcourue;
    private TypeVehicule typeDeVehicule;

    @Override
    public double calculerImpact() {
        double impactPerKm = 0.0;
        switch (typeDeVehicule) {
            case VOITURE:
                impactPerKm = 0.5;
                break;
            case TRAIN:
                impactPerKm = 0.1;
                break;
        }
        return impactPerKm * distanceParcourue;
    }
    @Override
    public void setImpact(double impact) {
        super.setImpact(impact);
    }
    @Override
    public TypeConsommation getTypeConsumption() {
        return TypeConsommation.ALIMENTATION;
    }

    public double getDistanceParcourue() { return distanceParcourue; }
    public void setDistanceParcourue(double distanceParcourue) { this.distanceParcourue = distanceParcourue; }
    public TypeVehicule getTypeDeVehicule() { return typeDeVehicule; }
    public void setTypeDeVehicule(TypeVehicule typeDeVehicule) { this.typeDeVehicule = typeDeVehicule; }
    @Override
    public String toString() {
        return "Transport{" +
                 "id=" + getId() +
                ", user=" + getUser() +
                ", typeConsumption=" + getTypeConsumption() +
                ", impact=" + getImpact() +
                ", startDate=" + getStartDate() +
                ", endDate=" + getEndDate() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", distanceParcourue=" + distanceParcourue +
                ", type_de_vehicule=" + typeDeVehicule +
                ", impact=" + calculerImpact() +
                '}';
    }
}
