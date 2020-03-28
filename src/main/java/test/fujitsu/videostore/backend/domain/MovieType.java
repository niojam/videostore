package test.fujitsu.videostore.backend.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Movie type
 * According that movie rent price should be calculated
 */
public enum MovieType {

    @JsonProperty("1") NEW(1, "New release"),
    @JsonProperty("2") REGULAR(2, "Regular rental"),
    @JsonProperty("3") OLD(3, "Old film");

    /**
     * Movie type representation in database
     */
    private final int databaseId;

    /**
     * Textural representation in database
     */
    private final String textualRepresentation;

    MovieType(int databaseId, String textualRepresentation) {
        this.databaseId = databaseId;
        this.textualRepresentation = textualRepresentation;
    }

    public String getTextualRepresentation() {
        return textualRepresentation;
    }

    public int getDatabaseId() {
        return databaseId;
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
