package com.example.greenplusdb.model.enums;

public enum TypeAliment {
    VIANDE("VIANDE"),
    LEGUME("LEGUME");

    private final String value;

    TypeAliment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static TypeAliment fromValue(String value) {
        for (TypeAliment type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown aliment type: " + value);
    }
}
