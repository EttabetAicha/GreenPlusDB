    package com.example.greenplusdb.model.enums;

    public enum TypeConsommation {
        TRANSPORT("TRANSPORT"),
        LOGEMENT("LOGEMENT"),
        ALIMENTATION("ALIMENTATION");

        private final String value;

        TypeConsommation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static TypeConsommation fromValue(String value) {
            for (TypeConsommation type : values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown consommation type: " + value);
        }
    }
