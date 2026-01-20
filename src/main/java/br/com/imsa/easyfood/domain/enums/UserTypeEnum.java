package br.com.imsa.easyfood.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    CLIENT("C", "Cliente"),
    OWNER("O", "Dono de Restaurante");

    private String acronym;
    private String description;

    public static UserTypeEnum getByAcronym(String acronym) {
        return Arrays.stream(UserTypeEnum.values())
                .filter(u -> u.getAcronym().equalsIgnoreCase(acronym))
                .findFirst()
                .orElse(null);
    }

    public static UserTypeEnum getByDescription(String description) {
        return Arrays.stream(UserTypeEnum.values())
                .filter(u -> u.getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElse(null);
    }
}
