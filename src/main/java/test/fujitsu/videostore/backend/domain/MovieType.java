package test.fujitsu.videostore.backend.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import test.fujitsu.videostore.backend.reciept.price.NewRealisePrice;
import test.fujitsu.videostore.backend.reciept.price.OldFilmPrice;
import test.fujitsu.videostore.backend.reciept.price.PriceCalculationStrategy;
import test.fujitsu.videostore.backend.reciept.price.RegularFilmPrice;

/**
 * Movie type
 * According that movie rent price should be calculated
 */
public enum MovieType {

    @JsonProperty("1") NEW(1, "New release", NewRealisePrice.getInstance()),
    @JsonProperty("2") REGULAR(2, "Regular rental", RegularFilmPrice.getInstance()),
    @JsonProperty("3") OLD(3, "Old film", OldFilmPrice.getInstance());

    /**
     * Movie type representation in database
     */
    private final int databaseId;

    /**
     * Textural representation in database
     */
    private final String textualRepresentation;

    /**
     * Strategy for movie price calculation
     */
    private final PriceCalculationStrategy priceCalculationStrategy;

    MovieType(int databaseId, String textualRepresentation, PriceCalculationStrategy priceCalculationStrategy) {
        this.databaseId = databaseId;
        this.textualRepresentation = textualRepresentation;
        this.priceCalculationStrategy = priceCalculationStrategy;
    }

    public String getTextualRepresentation() {
        return textualRepresentation;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public PriceCalculationStrategy getPriceCalculationStrategy() {
        return priceCalculationStrategy;
    }

    @JsonCreator
    public static MovieType getNameByValue(final int value) {
        for (final MovieType m : MovieType.values()) {
            if (m.getDatabaseId() == value) {
                return m;
            }
        }
        return null;
    }
}
