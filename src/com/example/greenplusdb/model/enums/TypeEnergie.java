package com.example.greenplusdb.model.enums;

public enum TypeEnergie {
    ELECTRICITE("ElECTRICITE"),
    GAZ("GAZ");

    private final String value;

    TypeEnergie(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static TypeEnergie fromValue(String value) {
        for (TypeEnergie type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown energy type: " + value);
    }
}
