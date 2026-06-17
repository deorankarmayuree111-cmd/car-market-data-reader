import java.util.List;
import java.util.Collections;

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

	PricingStrategy getPricing();

	default String getCurrencySymbol() {
		return getPricing().currencySymbol();
	}

	default double getTaxRate() {
		return getPricing().taxRate();
	}

	default double getBasePriceUSD() {
		return getPricing().basePrice();
	}

	default double getTaxAmountUSD() {
		TaxCalculator calculator = getPricing().getTaxCalculator();
		return calculator.calculate(getBasePriceUSD(), getPricing().taxRate());
	}

	default double getTotalPriceUSD() {
		TaxCalculator calculator = getPricing().getTaxCalculator();
		return calculator.totalPrice(getBasePriceUSD(), getPricing().taxRate());
	}

	default double getBasePriceLocal() {
		return getBasePriceUSD() * getPricing().exchangeRate();
	}

	default double toLocalCurrency(double usdAmount) {
		return usdAmount * getPricing().exchangeRate();
	}

	default double getTaxAmountLocal() {
		return toLocalCurrency(getTaxAmountUSD());
	}

	default double getTotalPriceLocal() {
		return toLocalCurrency(getTotalPriceUSD());
	}
}

class CarEntry implements Car {
	private final String id;
	private final String make;
	private final String model;
	private final int year;
	private final String countryOfOrigin;
	private final String fuelType;
	private final String transmission;
	private final List<String> tags;
	private final String status;
	private final Region region;
	private final PricingStrategy pricing;

	public CarEntry(
			String id,
			String make,
			String model,
			int year,
			String countryOfOrigin,
			String fuelType,
			String transmission,
			List<String> tags,
			String status,
			double basePrice,
			double taxRate) {
		this.id = id;
		this.make = make;
		this.model = model;
		this.year = year;
		this.countryOfOrigin = countryOfOrigin;
		this.fuelType = fuelType;
		this.transmission = transmission;
		this.tags = tags;
		this.status = status;
		// Region and pricing derived from country + CSV price data
		this.region = RegionFactory.getRegion(countryOfOrigin);
		this.pricing = RegionFactory.getStrategy(region, basePrice, taxRate);
	}

	public String getId() {
		return id;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public int getYear() {
		return year;
	}

	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public String getFuelType() {
		return fuelType;
	}

	public String getTransmission() {
		return transmission;
	}

	public List<String> getTags() {
		return Collections.unmodifiableList(tags);
	}

	public String getStatus() {
		return status;
	}

	@Override
	public Region getRegion() {
		return region;
	}

	@Override
	public PricingStrategy getPricing() {
		return pricing;
	}
}