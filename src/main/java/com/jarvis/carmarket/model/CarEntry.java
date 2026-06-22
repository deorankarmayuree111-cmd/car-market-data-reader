package com.jarvis.carmarket.model;

import com.jarvis.carmarket.pricing.PricingStrategy;

import java.util.Collections;
import java.util.List;

public class CarEntry implements Car {
    private final String id;
    private final String make;
    private final String model;
    private final int year;
    private final String countryOfOrigin;
    private final String fuelType;
    private final String transmission;
    private final List<String> tags;
    private final String status;
    private final double basePrice;
    private final Region region;
    private final PricingStrategy pricing;

    public CarEntry(
            String id, String make, String model, int year,
            String countryOfOrigin, String fuelType, String transmission,
            List<String> tags, String status, double basePrice, double taxRate) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.countryOfOrigin = countryOfOrigin;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.tags = tags;
        this.status = status;
        this.basePrice = basePrice;
        this.region = PricingStrategy.regionFor(countryOfOrigin);
        this.pricing = PricingStrategy.forRegion(region, taxRate);
    }

    @Override public String getId()              { return id; }
    @Override public String getMake()            { return make; }
    @Override public String getModel()           { return model; }
    @Override public int getYear()               { return year; }
    @Override public String getCountryOfOrigin() { return countryOfOrigin; }
    @Override public String getFuelType()        { return fuelType; }
    @Override public String getTransmission()    { return transmission; }
    @Override public String getStatus()          { return status; }
    @Override public double getBasePrice()       { return basePrice; }
    @Override public Region getRegion()          { return region; }
    @Override public PricingStrategy getPricing(){ return pricing; }

    @Override
    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }
}
