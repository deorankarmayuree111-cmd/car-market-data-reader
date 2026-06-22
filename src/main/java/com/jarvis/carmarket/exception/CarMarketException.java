package com.jarvis.carmarket.exception;

public class CarMarketException extends RuntimeException {

    public CarMarketException(String message) {
        super(message);
    }

    public static CarMarketException unknownCountry(String country) {
        return new CarMarketException("Unknown country: " + country);
    }

    public static CarMarketException malformedRow(String row) {
        return new CarMarketException("Malformed CSV row: " + row);
    }
}
