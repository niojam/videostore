package test.fujitsu.videostore.backend.reciept.price;

import java.math.BigDecimal;

public class OldFilmPrice implements PriceCalculationStrategy {

    private static final Integer DAYS_WITH_BASIC_PRICE = 5;

    private static OldFilmPrice instance = new OldFilmPrice();

    private OldFilmPrice() {
    }

    public static OldFilmPrice getInstance() {
        return instance;
    }

    @Override
    public BigDecimal getPrice(Integer daysRented) {
        return daysRented > DAYS_WITH_BASIC_PRICE ? BASIC_PRICE
                .add(BASIC_PRICE.multiply(BigDecimal.valueOf(daysRented - DAYS_WITH_BASIC_PRICE))) : BASIC_PRICE;
    }

    @Override
    public Integer getBonus() {
        return BASIC_BONUS;
    }
}
