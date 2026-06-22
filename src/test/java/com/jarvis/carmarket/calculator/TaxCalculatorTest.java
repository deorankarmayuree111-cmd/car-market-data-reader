package com.jarvis.carmarket.calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaxCalculatorTest {

    private final TaxCalculator calculator = (basePrice, taxRate) -> basePrice * taxRate;

    @Test
    void calculate_returnsCorrectTaxAmount() {
        assertEquals(2000.0, calculator.calculate(20000.0, 0.1), 0.01);
    }

    @Test
    void totalPrice_returnsSumOfBaseAndTax() {
        assertEquals(22000.0, calculator.totalPrice(20000.0, 0.1), 0.01);
    }

    @Test
    void validate_throwsOnNegativeBasePrice() {
        assertThrows(IllegalArgumentException.class, () -> calculator.validate(-1, 0.1));
    }

    @Test
    void validate_throwsWhenTaxRateExceedsOne() {
        assertThrows(IllegalArgumentException.class, () -> calculator.validate(1000, 1.5));
    }
}
