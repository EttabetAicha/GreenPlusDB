package com.example.greenplusdb.model;

public class Transport extends Consommation {
    private TypeVehicule typeVehicule;
    private double distanceParcourue;


    public Transport(User user, TypeVehicule typeVehicule, double distanceParcourue) {

        super(user, TypeConsommation.TRANSPORT, calculateTransportImpact(distanceParcourue, typeVehicule));
        this.typeVehicule = typeVehicule;
        this.distanceParcourue = distanceParcourue;
    }


    public TypeVehicule getTypeVehicule() {
        return typeVehicule;
    }

    public void setTypeVehicule(TypeVehicule typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public double getDistanceParcourue() {
        return distanceParcourue;
    }

    public void setDistanceParcourue(double distanceParcourue) {
        this.distanceParcourue = distanceParcourue;
    }

    private static double calculateTransportImpact(double distanceParcourue, TypeVehicule typeVehicule) {
        double impact = 0;
        switch (typeVehicule) {
            case VOITURE:
                impact = distanceParcourue * 0.5;
                break;
            case TRAIN:
                impact = distanceParcourue * 0.1;
                break;
        }
        return impact;
    }

    @Override
    public double calculerImpact() {
        return calculateTransportImpact(distanceParcourue, typeVehicule);
    }
}
