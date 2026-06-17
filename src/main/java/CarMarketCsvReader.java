import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CarMarketCsvReader {

	public static class CarRecord {
		public final String id;
		public final String country;
		public final String regionName;
		public final String regionCode;
		public final String carManufacturer;
		public final String carModel;
		public final double basePrice;
		public final double taxRate;
		public final double taxAmount;
		public final double totalPrice;
		public final String fuelType;
		public final String transmission;
		public final int manufacturingYear;
		public final String tag;
		public final String status;

		public CarRecord(String[] cols) {
			this.id = cols[0].trim();
			this.country = cols[1].trim();
			this.regionName = cols[2].trim();
			this.regionCode = cols[3].trim();
			this.carManufacturer = cols[4].trim();
			this.carModel = cols[5].trim();
			this.basePrice = Double.parseDouble(cols[6].trim());
			this.taxRate = Double.parseDouble(cols[7].trim());
			this.taxAmount = Double.parseDouble(cols[8].trim());
			this.totalPrice = Double.parseDouble(cols[9].trim());
			this.fuelType = cols[10].trim();
			this.transmission = cols[11].trim();
			this.manufacturingYear = Integer.parseInt(cols[12].trim());
			this.tag = cols[13].trim();
			this.status = cols[14].trim();
		}

		// Splits "Premium|Electric-Vehicle" → ["Premium", "Electric-Vehicle"]
		public List<String> getTagList() {
			return Arrays.stream(tag.split("\\|"))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.collect(Collectors.toList());
		}

		// Converts this raw CSV record into a fully wired CarEntry
		public CarEntry toCarEntry() {
			return new CarEntry(
					id,
					carManufacturer, // make
					carModel, // model
					manufacturingYear, // year
					country,
					fuelType,
					transmission,
					getTagList(), // tags — split from pipe-separated string
					status,
					basePrice, // passed into PricingStrategy constructor
					taxRate // passed into PricingStrategy constructor
			);
		}

		@Override
		public String toString() {
			return String.format(
					"[%s] %s %s (%d) | %s | %s | Base: $%.2f | Total: $%.2f | Status: %s",
					id, carManufacturer, carModel, manufacturingYear,
					country, fuelType, basePrice, totalPrice, status);
		}
	}

	// Read all raw CarRecords from CSV
	public static List<CarRecord> readAll(String filePath)
			throws IOException, CsvValidationException {

		Path path = Paths.get(filePath);
		List<CarRecord> records = new ArrayList<>();

		try (Reader nioReader = Files.newBufferedReader(path);
				CSVReader csvReader = new CSVReader(nioReader)) {

			String[] headers = csvReader.readNext();
			if (headers == null)
				throw new IOException("CSV file is empty: " + filePath);

			String[] cols;
			while ((cols = csvReader.readNext()) != null) {
				if (cols.length < 15) {
					System.err.println("Skipping malformed row: "
							+ String.join(",", cols));
					continue;
				}
				records.add(new CarRecord(cols));
			}
		}
		return records;
	}

	// Read all records and convert directly to CarEntry objects
	public static List<CarEntry> readAllAsCarEntries(String filePath)
			throws IOException, CsvValidationException {
		return readAll(filePath).stream()
				.map(record -> record.toCarEntry())
				.collect(Collectors.toList());
	}

	public static void main(String[] args) throws IOException, CsvValidationException {

		String csvPath = "C:/Users/eoz2kor/Desktop/Workflow/car_market_processing_data.csv";
		if (args.length > 0)
			csvPath = args[0];

		System.out.println("Reading: " + csvPath);

		// ── Raw CarRecord output (as before) ─────────────────────────────
		List<CarRecord> rawRecords = readAll(csvPath);
		System.out.printf("Total records loaded: %d%n%n", rawRecords.size());

		System.out.println("=== First 5 raw records ===");
		rawRecords.stream().limit(5).forEach(System.out::println);

		// ── Wired CarEntry output (uses full data model) ──────────────────
		List<CarEntry> cars = readAllAsCarEntries(csvPath);

		System.out.println("\n=== First 5 CarEntry records (with pricing) ===");
		cars.stream().limit(5).forEach(car -> System.out.printf(
				"[%s] %s %s | Region: %s | Tags: %s | Fuel: %s%n" +
						"     Base: $%.2f | Tax(%.0f%%): $%.2f | Total USD: $%.2f | Total Local: %s%.2f%n",
				car.getId(), car.getMake(), car.getModel(),
				car.getRegion().getLabel(),
				car.getTags(),
				car.getFuelType(),
				car.getBasePriceUSD(),
				car.getTaxRate() * 100,
				car.getTaxAmountUSD(),
				car.getTotalPriceUSD(),
				car.getCurrencySymbol(),
				car.getTotalPriceLocal()));

		long activeCount = rawRecords.stream()
				.filter(c -> "Active".equalsIgnoreCase(c.status))
				.count();
		System.out.printf("%nActive cars: %d%n", activeCount);

		double avgPrice = rawRecords.stream()
				.mapToDouble(c -> c.totalPrice)
				.average()
				.orElse(0.0);
		System.out.printf("Average total price: $%.2f%n", avgPrice);
	}
}