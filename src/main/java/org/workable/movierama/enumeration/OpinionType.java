package org.workable.movierama.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum OpinionType {

    LIKE("L"),
    HATE("H");

    private final String type;

    public static OpinionType fromType(String type) {
        return Arrays.stream(values())
                .filter(opinionType -> opinionType.getType().equalsIgnoreCase(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
    }

}
