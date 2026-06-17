import java.util.*;
import java.util.function.BiFunction;

public class RegionFactory {

    // Maps lowercase country name → Region enum
    private static final Map<String, Region> COUNTRY_REGION_MAP = new HashMap<>();

    // BiFunction<basePrice, taxRate, PricingStrategy>
    // Takes two arguments (basePrice, taxRate) and returns a PricingStrategy
    private static final Map<Region, BiFunction<Double, Double, PricingStrategy>> STRATEGY_MAP = Map.of(
            Region.R1, R1PricingStrategy::new,
            Region.R2, R2PricingStrategy::new,
            Region.R3, R3PricingStrategy::new,
            Region.R4, R4PricingStrategy::new,
            Region.R5, R5PricingStrategy::new,
            Region.R6, R6PricingStrategy::new
    );

    static {
        // R1 — Asia
        for (String c : new String[]{
                "china", "japan", "india", "south korea",
                "indonesia", "malaysia", "thailand"})
            COUNTRY_REGION_MAP.put(c, Region.R1);

        // R2 — Europe
        for (String c : new String[]{
                "germany", "france", "united kingdom", "italy",
                "spain", "netherlands", "sweden"})
            COUNTRY_REGION_MAP.put(c, Region.R2);

        // R3 — North America
        for (String c : new String[]{
                "usa", "united states", "canada", "mexico"})
            COUNTRY_REGION_MAP.put(c, Region.R3);

        // R4 — South America
        for (String c : new String[]{
                "brazil", "argentina", "chile", "colombia"})
            COUNTRY_REGION_MAP.put(c, Region.R4);

        // R5 — Africa
        for (String c : new String[]{
                "south africa", "nigeria", "kenya",
                "egypt", "ghana", "morocco"})
            COUNTRY_REGION_MAP.put(c, Region.R5);

        // R6 — Oceania
        for (String c : new String[]{
                "australia", "new zealand"})
            COUNTRY_REGION_MAP.put(c, Region.R6);
    }

    // Looks up Region by country name from CSV
    public static Region getRegion(String country) {
        Region r = COUNTRY_REGION_MAP.get(country.trim().toLowerCase());
        if (r == null)
            throw new IllegalArgumentException("Unknown country: " + country);
        return r;
    }

    public static PricingStrategy getStrategy(Region region, double basePrice, double taxRate) {
        BiFunction<Double, Double, PricingStrategy> fn = STRATEGY_MAP.get(region);
        if (fn == null)
            throw new IllegalArgumentException("No strategy for region: " + region);
        return fn.apply(basePrice, taxRate);
    }

    public static PricingStrategy getStrategy(String country, double basePrice, double taxRate) {
        return getStrategy(getRegion(country), basePrice, taxRate);
    }
}