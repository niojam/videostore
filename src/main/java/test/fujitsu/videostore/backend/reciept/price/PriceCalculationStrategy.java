package test.fujitsu.videostore.backend.reciept.price;

import java.math.BigDecimal;

public interface PriceCalculationStrategy {
    BigDecimal PREMIUM_PRICE = BigDecimal.valueOf(4);
    BigDecimal BASIC_PRICE = BigDecimal.valueOf(3);
    int BASIC_BONUS = 1;

    BigDecimal getPrice(Integer daysRented);

    Integer getBonus();

}
