package com.example.greenplusdb.model;

public enum TypeConsommation {
    TRANSPORT("Transport"),
    LOGEMENT("Logement"),
    ALIMENTATION("Alimentation");

    private  String displayName;



    TypeConsommation(String transport) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
