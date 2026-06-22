package com.jarvis.carmarket.calculator;

@FunctionalInterface
public interface TaxCalculator {

    double calculate(double basePrice, double taxRate);

    default double totalPrice(double basePrice, double taxRate) {
        return round(basePrice + calculate(basePrice, taxRate));
    }

    default double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    default void validate(double basePrice, double taxRate) {
        if (basePrice < 0)
            throw new IllegalArgumentException("basePrice cannot be negative: " + basePrice);
        if (taxRate < 0 || taxRate > 1.0)
            throw new IllegalArgumentException("taxRate must be between 0 and 1: " + taxRate);
    }
}
