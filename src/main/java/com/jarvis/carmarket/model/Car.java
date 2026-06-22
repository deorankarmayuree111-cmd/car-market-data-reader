package com.jarvis.carmarket.model;

import com.jarvis.carmarket.pricing.PricingStrategy;

import java.util.List;

public interface Car {
    String getId();
    String getMake();
    String getModel();
    int getYear();
    String getCountryOfOrigin();
    String getFuelType();
    String getTransmission();
    List<String> getTags();
    String getStatus();
    Region getRegion();
    double getBasePrice();
    PricingStrategy getPricing();

    default double getTaxAmount()      { return getPricing().tax(getBasePrice()); }
    default double getTotalPriceUSD()  { return getPricing().total(getBasePrice()); }
    default double getTotalPriceLocal(){ return getPricing().toLocal(getTotalPriceUSD()); }
    default String getCurrencySymbol() { return getPricing().currencySymbol(); }
}
