package com.jarvis.carmarket.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarEntryTest {

    private CarEntry buildCar(String country) {
        return new CarEntry(
                "CAR-001", "Toyota", "Corolla", 2022,
                country, "Petrol", "Automatic",
                List.of("Premium"), "Active", 20000.0, 0.1);
    }

    @Test
    void getRegion_resolvesFromCountry() {
        assertEquals(Region.R1, buildCar("Japan").getRegion());
        assertEquals(Region.R2, buildCar("Germany").getRegion());
    }

    @Test
    void getTags_isUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> buildCar("Japan").getTags().add("Extra"));
    }

    @Test
    void getTotalPriceLocal_appliesExchangeRate() {
        CarEntry car = buildCar("Germany");
        double expected = car.getTotalPriceUSD() * car.getPricing().exchangeRate();
        assertEquals(expected, car.getTotalPriceLocal(), 0.01);
    }
}
