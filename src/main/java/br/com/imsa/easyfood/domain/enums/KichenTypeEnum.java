package br.com.imsa.easyfood.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum KichenTypeEnum {

    ITALIAN("I", "Italiana"),
    BRAZILIAN("B", "Brasileira");

    private String acronym;
    private String description;

    public static KichenTypeEnum getByAcronym(String acronym) {
        return Arrays.stream(KichenTypeEnum.values())
                .filter(u -> u.getAcronym().equalsIgnoreCase(acronym))
                .findFirst()
                .orElse(null);
    }

    public static KichenTypeEnum getByDescription(String description) {
        return Arrays.stream(KichenTypeEnum.values())
                .filter(u -> u.getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElse(null);
    }
}
