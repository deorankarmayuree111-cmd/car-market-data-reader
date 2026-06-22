package com.jarvis.carmarket.pricing;

import com.jarvis.carmarket.model.Region;

import java.util.Map;

@FunctionalInterface
public interface PricingStrategy {

    double taxRate();

    default double tax(double basePrice) {
        return basePrice * taxRate();
    }

    default double total(double basePrice) {
        return basePrice + tax(basePrice);
    }

    default String currencySymbol() {
        return "$";
    }

    default double exchangeRate() {
        return 1.0;
    }

    default double toLocal(double usdAmount) {
        return usdAmount * exchangeRate();
    }

    static PricingStrategy of(double taxRate, String currencySymbol, double exchangeRate) {
        return new PricingStrategy() {
            @Override public double taxRate()        { return taxRate; }
            @Override public String currencySymbol() { return currencySymbol; }
            @Override public double exchangeRate()   { return exchangeRate; }
        };
    }

    static PricingStrategy forRegion(Region region, double taxRate) {
        return switch (region) {
            case R1 -> of(taxRate, "¥",  7.25);
            case R2 -> new PricingStrategy() {
                @Override public double taxRate()        { return taxRate; }
                @Override public String currencySymbol() { return "€"; }
                @Override public double exchangeRate()   { return 0.93; }
                @Override public double tax(double basePrice) {
                    return basePrice * taxRate + basePrice * 0.05;
                }
            };
            case R3 -> of(taxRate, "$",  1.0);
            case R4 -> of(taxRate, "R$", 5.0);
            case R5 -> of(taxRate, "R",  18.5);
            case R6 -> of(taxRate, "A$", 1.53);
        };
    }

    Map<String, Region> COUNTRY_REGION_MAP = Map.ofEntries(
            Map.entry("china",          Region.R1),
            Map.entry("japan",          Region.R1),
            Map.entry("india",          Region.R1),
            Map.entry("south korea",    Region.R1),
            Map.entry("indonesia",      Region.R1),
            Map.entry("malaysia",       Region.R1),
            Map.entry("thailand",       Region.R1),
            Map.entry("germany",        Region.R2),
            Map.entry("france",         Region.R2),
            Map.entry("united kingdom", Region.R2),
            Map.entry("italy",          Region.R2),
            Map.entry("spain",          Region.R2),
            Map.entry("netherlands",    Region.R2),
            Map.entry("sweden",         Region.R2),
            Map.entry("usa",            Region.R3),
            Map.entry("united states",  Region.R3),
            Map.entry("canada",         Region.R3),
            Map.entry("mexico",         Region.R3),
            Map.entry("brazil",         Region.R4),
            Map.entry("argentina",      Region.R4),
            Map.entry("chile",          Region.R4),
            Map.entry("colombia",       Region.R4),
            Map.entry("south africa",   Region.R5),
            Map.entry("nigeria",        Region.R5),
            Map.entry("kenya",          Region.R5),
            Map.entry("egypt",          Region.R5),
            Map.entry("ghana",          Region.R5),
            Map.entry("morocco",        Region.R5),
            Map.entry("australia",      Region.R6),
            Map.entry("new zealand",    Region.R6)
    );

    static Region regionFor(String country) {
        Region r = COUNTRY_REGION_MAP.get(country.trim().toLowerCase());
        if (r == null) throw new IllegalArgumentException("Unknown country: " + country);
        return r;
    }
}
