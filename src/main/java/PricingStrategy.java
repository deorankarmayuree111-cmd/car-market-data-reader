interface PricingStrategy {
    double basePrice();
    double taxRate();
    String currencySymbol();
    double exchangeRate();

    default TaxCalculator getTaxCalculator() {
        return (basePrice, taxRate) -> basePrice * taxRate;
    }
}

// R1 — Asia
class R1PricingStrategy implements PricingStrategy {
    private final double basePrice;
    private final double taxRate;

    public R1PricingStrategy(double basePrice, double taxRate) {
        this.basePrice = basePrice;
        this.taxRate   = taxRate;
    }

    public double basePrice()       { return basePrice; }
    public double taxRate()         { return taxRate; }
    public String currencySymbol()  { return "¥"; }
    public double exchangeRate()    { return 7.25; }
}

// R2 — Europe
class R2PricingStrategy implements PricingStrategy {
    private final double basePrice;
    private final double taxRate;

    public R2PricingStrategy(double basePrice, double taxRate) {
        this.basePrice = basePrice;
        this.taxRate   = taxRate;
    }

    public double basePrice()       { return basePrice; }
    public double taxRate()         { return taxRate; }
    public String currencySymbol()  { return "€"; }
    public double exchangeRate()    { return 0.93; }

    @Override
    public TaxCalculator getTaxCalculator() {
        return (basePrice, taxRate) -> {
            double vat         = basePrice * taxRate;
            double importDuty  = basePrice * 0.05;
            return vat + importDuty;
        };
    }
}

// R3 — North America
class R3PricingStrategy implements PricingStrategy {
    private final double basePrice;
    private final double taxRate;

    public R3PricingStrategy(double basePrice, double taxRate) {
        this.basePrice = basePrice;
        this.taxRate   = taxRate;
    }

    public double basePrice()       { return basePrice; }
    public double taxRate()         { return taxRate; }
    public String currencySymbol()  { return "$"; }
    public double exchangeRate()    { return 1.0; }
}

// R4 — South America
class R4PricingStrategy implements PricingStrategy {
    private final double basePrice;
    private final double taxRate;

    public R4PricingStrategy(double basePrice, double taxRate) {
        this.basePrice = basePrice;
        this.taxRate   = taxRate;
    }

    public double basePrice()       { return basePrice; }
    public double taxRate()         { return taxRate; }
    public String currencySymbol()  { return "R$"; }
    public double exchangeRate()    { return 5.0; }
}

// R5 — Africa
class R5PricingStrategy implements PricingStrategy {
    private final double basePrice;
    private final double taxRate;

    public R5PricingStrategy(double basePrice, double taxRate) {
        this.basePrice = basePrice;
        this.taxRate   = taxRate;
    }

    public double basePrice()       { return basePrice; }
    public double taxRate()         { return taxRate; }
    public String currencySymbol()  { return "R"; }
    public double exchangeRate()    { return 18.5; }
}

// R6 — Oceania
class R6PricingStrategy implements PricingStrategy {
    private final double basePrice;
    private final double taxRate;

    public R6PricingStrategy(double basePrice, double taxRate) {
        this.basePrice = basePrice;
        this.taxRate   = taxRate;
    }

    public double basePrice()       { return basePrice; }
    public double taxRate()         { return taxRate; }
    public String currencySymbol()  { return "A$"; }
    public double exchangeRate()    { return 1.53; }
}