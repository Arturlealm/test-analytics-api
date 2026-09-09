package tech.magicbook.analytics.api.entity.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AgeRange {

    UNDER_18("UNDER_18"),
    AGE_18_24("18_24"),
    AGE_25_34("25_34"),
    AGE_35_44("35_44"),
    AGE_45_54("45_54"),
    AGE_55_PLUS("55_PLUS"),
    UNKNOWN("UNKNOWN");

    private final String value;

    AgeRange(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AgeRange fromValue(String value) {
        return Arrays.stream(values())
            .filter(ageRange -> ageRange.value.equals(value))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException("Invalid age range: " + value)
            );
    }
}