package com.jarvis.carmarket.reader;

import com.jarvis.carmarket.model.Region;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarMarketCsvReaderTest {

    private static final String[] GERMANY_ROW = {
        "CAR-001", "Germany", "Europe", "R2", "BMW", "3 Series",
        "35000.0", "0.19", "6650.0", "41650.0",
        "Petrol", "Automatic", "2023", "Premium|Luxury", "Active"
    };

    private static final String[] JAPAN_ROW = {
        "CAR-002", "Japan", "Asia", "R1", "Toyota", "Corolla",
        "20000.0", "0.10", "2000.0", "22000.0",
        "Hybrid", "Automatic", "2022", "Eco", "Active"
    };

    @Test
    void parsesColumnsCorrectly() {
        CarRecord record = new CarRecord(GERMANY_ROW);
        assertEquals("CAR-001", record.id);
        assertEquals("BMW", record.carManufacturer);
        assertEquals(35000.0, record.basePrice, 0.001);
        assertEquals(2023, record.manufacturingYear);
        assertEquals("Active", record.status);
    }

    @Test
    void getTagList_splitsByPipe() {
        assertEquals(List.of("Premium", "Luxury"), new CarRecord(GERMANY_ROW).getTagList());
    }

    @Test
    void toCarEntry_convertsSuccessfully() {
        var entry = new CarRecord(GERMANY_ROW).toCarEntry();
        assertEquals("BMW", entry.getMake());
        assertEquals("3 Series", entry.getModel());
        assertNotNull(entry.getPricing());
        assertEquals(Region.R2, entry.getRegion());
    }

    @Test
    void toCarEntry_japanRow_resolvesAsiaRegion() {
        var entry = new CarRecord(JAPAN_ROW).toCarEntry();
        assertEquals("Toyota", entry.getMake());
        assertEquals(Region.R1, entry.getRegion());
    }
}
