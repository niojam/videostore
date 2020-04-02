package test.fujitsu.videostore.backend.reciept.price;

import java.math.BigDecimal;

public class NewRealisePrice implements PriceCalculationStrategy {
    public static final int NEW_REALISE_BONUS = 2;
    private static NewRealisePrice instance = new NewRealisePrice();

    private NewRealisePrice() {
    }

    public static NewRealisePrice getInstance() {
        return instance;
    }

    @Override
    public BigDecimal getPrice(Integer daysRented) {
       return PREMIUM_PRICE.multiply(BigDecimal.valueOf(daysRented));
    }

    @Override
    public Integer getBonus() {
        return NEW_REALISE_BONUS;
    }
}
