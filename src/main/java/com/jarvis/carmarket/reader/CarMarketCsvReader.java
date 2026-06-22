package com.jarvis.carmarket.reader;

import com.jarvis.carmarket.model.Car;
import com.jarvis.carmarket.model.CarEntry;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarMarketCsvReader {

    public static List<CarRecord> readAll(String filePath) throws IOException, CsvValidationException {
        List<CarRecord> records = new ArrayList<>();

        try (Reader nioReader = Files.newBufferedReader(Paths.get(filePath));
             CSVReader csvReader = new CSVReader(nioReader)) {

            String[] headers = csvReader.readNext();
            if (headers == null) throw new IOException("CSV file is empty: " + filePath);

            String[] cols;
            while ((cols = csvReader.readNext()) != null) {
                if (cols.length < 15) {
                    System.err.println("Skipping malformed row: " + String.join(",", cols));
                    continue;
                }
                records.add(new CarRecord(cols));
            }
        }
        return records;
    }

    public static List<CarEntry> readAllAsCarEntries(String filePath) throws IOException, CsvValidationException {
        return readAll(filePath).stream()
                .map(CarRecord::toCarEntry)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException, CsvValidationException {
        String csvPath = args.length > 0 ? args[0] : "car_market_processing_data.csv";

        List<CarRecord> rawRecords = readAll(csvPath);
        System.out.printf("Total records loaded: %d%n%n", rawRecords.size());

        rawRecords.stream().limit(5).forEach(System.out::println);

        List<CarEntry> cars = readAllAsCarEntries(csvPath);
        cars.stream().limit(5).forEach(car -> System.out.printf(
                "[%s] %s %s | %s | Base: $%.2f | Tax: $%.2f | Total: $%.2f | Local: %s%.2f%n",
                car.getId(), car.getMake(), car.getModel(),
                car.getRegion().getLabel(),
                car.getBasePrice(), car.getTaxAmount(),
                car.getTotalPriceUSD(), car.getCurrencySymbol(), car.getTotalPriceLocal()));

        long active = rawRecords.stream().filter(c -> "Active".equalsIgnoreCase(c.status)).count();
        double avgPrice = rawRecords.stream().mapToDouble(c -> c.totalPrice).average().orElse(0);

        System.out.printf("%nActive cars: %d%n", active);
        System.out.printf("Average total price: $%.2f%n", avgPrice);
    }
}
