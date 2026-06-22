package com.jarvis.carmarket.reader;

import com.jarvis.carmarket.model.CarEntry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CarRecord {
    public final String id;
    public final String country;
    public final String carManufacturer;
    public final String carModel;
    public final double basePrice;
    public final double taxRate;
    public final double totalPrice;
    public final String fuelType;
    public final String transmission;
    public final int manufacturingYear;
    public final String tag;
    public final String status;

    public CarRecord(String[] cols) {
        this.id = cols[0].trim();
        this.country = cols[1].trim();
        this.carManufacturer = cols[4].trim();
        this.carModel = cols[5].trim();
        this.basePrice = Double.parseDouble(cols[6].trim());
        this.taxRate = Double.parseDouble(cols[7].trim());
        this.totalPrice = Double.parseDouble(cols[9].trim());
        this.fuelType = cols[10].trim();
        this.transmission = cols[11].trim();
        this.manufacturingYear = Integer.parseInt(cols[12].trim());
        this.tag = cols[13].trim();
        this.status = cols[14].trim();
    }

    public List<String> getTagList() {
        return Arrays.stream(tag.split("\\|"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public CarEntry toCarEntry() {
        return new CarEntry(
                id, carManufacturer, carModel, manufacturingYear,
                country, fuelType, transmission,
                getTagList(), status, basePrice, taxRate);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s (%d) | %s | %s | Base: $%.2f | Total: $%.2f | Status: %s",
                id, carManufacturer, carModel, manufacturingYear,
                country, fuelType, basePrice, totalPrice, status);
    }
}
