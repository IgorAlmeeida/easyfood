package br.com.imsa.easyfood.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AvailabilityEnum {
    DELIVERY("D", "Delivery"),
    LOCAL("L", "Local");

    private String acronym;
    private String description;

    public static AvailabilityEnum getByAcronym(String acronym) {
        return Arrays.stream(AvailabilityEnum.values())
                .filter(u -> u.getAcronym().equalsIgnoreCase(acronym))
                .findFirst()
                .orElse(null);
    }

    public static AvailabilityEnum getByDescription(String description) {
        return Arrays.stream(AvailabilityEnum.values())
                .filter(u -> u.getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElse(null);
    }
}
