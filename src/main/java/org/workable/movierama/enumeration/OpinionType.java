package org.workable.movierama.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Enum representing the types of opinions.
 */
@RequiredArgsConstructor
@Getter
public enum OpinionType {

    /**
     * Represents a 'like' opinion.
     */
    LIKE("L"),

    /**
     * Represents a 'hate' opinion
     */
    HATE("H");

    /**
     * The string representation of the opinion type.
     */
    private final String type;

    /**
     * Converts a string type into an {@link OpinionType} enum.
     *
     * @param type the string representation of the opinion type
     * @return the corresponding {@link OpinionType} enum
     * @throws IllegalArgumentException if the type is unknown
     */
    public static OpinionType fromType(String type) {
        return Arrays.stream(values())
                .filter(opinionType -> opinionType.getType().equalsIgnoreCase(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
    }

}
