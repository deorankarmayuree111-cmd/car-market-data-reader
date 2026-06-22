package com.jarvis.carmarket.pricing;

import com.jarvis.carmarket.model.Region;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PricingStrategyTest {

    @Test
    void regionFor_resolvesKnownCountries() {
        assertEquals(Region.R1, PricingStrategy.regionFor("japan"));
        assertEquals(Region.R2, PricingStrategy.regionFor("Germany"));
        assertEquals(Region.R3, PricingStrategy.regionFor("USA"));
        assertEquals(Region.R4, PricingStrategy.regionFor("BRAZIL"));
        assertEquals(Region.R5, PricingStrategy.regionFor("ghana"));
        assertEquals(Region.R6, PricingStrategy.regionFor("Australia"));
    }

    @Test
    void regionFor_throwsForUnknownCountry() {
        assertThrows(IllegalArgumentException.class, () -> PricingStrategy.regionFor("Atlantis"));
    }

    @Test
    void forRegion_europe_includesImportDuty() {
        PricingStrategy strategy = PricingStrategy.forRegion(Region.R2, 0.19);
        assertEquals(10000.0 * 0.19 + 10000.0 * 0.05, strategy.tax(10000.0), 0.01);
        assertEquals("€", strategy.currencySymbol());
        assertEquals(0.93, strategy.exchangeRate(), 0.001);
    }

    @Test
    void forRegion_asia_usesStandardTax() {
        PricingStrategy strategy = PricingStrategy.forRegion(Region.R1, 0.1);
        assertEquals(1000.0, strategy.tax(10000.0), 0.01);
        assertEquals("¥", strategy.currencySymbol());
    }

    @Test
    void forRegion_northAmerica_hasUsdRate() {
        PricingStrategy strategy = PricingStrategy.forRegion(Region.R3, 0.08);
        assertEquals(1.0, strategy.exchangeRate());
        assertEquals("$", strategy.currencySymbol());
    }

    @Test
    void total_returnsSumOfBasePlusTax() {
        PricingStrategy strategy = PricingStrategy.of(0.1, "$", 1.0);
        assertEquals(11000.0, strategy.total(10000.0), 0.01);
    }
}
