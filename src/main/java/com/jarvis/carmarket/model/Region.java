package com.jarvis.carmarket.model;

public enum Region {
    R1("Asia"),
    R2("Europe"),
    R3("North America"),
    R4("South America"),
    R5("Africa"),
    R6("Oceania");

    private final String label;

    Region(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
