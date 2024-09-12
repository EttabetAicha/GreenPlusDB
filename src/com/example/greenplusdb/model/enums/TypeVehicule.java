package com.example.greenplusdb.model.enums;

public enum TypeVehicule {

    VOITURE("VOITURE"),
    TRAIN("TRAIN");

    private final String value;

    TypeVehicule(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static TypeVehicule fromValue(String value) {
        for (TypeVehicule type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown transport type: " + value);
    }
}
