package test.fujitsu.videostore.backend.reciept.price;

import java.math.BigDecimal;

public class RegularFilmPrice implements PriceCalculationStrategy {
    private static final int DAYS_WITH_BASIC_PRICE = 3;
    private static RegularFilmPrice instance = new RegularFilmPrice();

    private RegularFilmPrice() {
    }

    public static RegularFilmPrice getInstance() {
        return instance;
    }

    @Override
    public BigDecimal getPrice(Integer daysRented) {
        if (daysRented <= 0) return BigDecimal.ZERO;
        return daysRented > DAYS_WITH_BASIC_PRICE ? BASIC_PRICE
                .add(BASIC_PRICE.multiply(BigDecimal.valueOf(daysRented - DAYS_WITH_BASIC_PRICE))) : BASIC_PRICE;
    }

    @Override
    public Integer getBonus() {
        return BASIC_BONUS;
    }

}
