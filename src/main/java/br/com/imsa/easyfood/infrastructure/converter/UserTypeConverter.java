package br.com.imsa.easyfood.infrastructure.converter;

import br.com.imsa.easyfood.domain.enums.UserTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(UserTypeEnum u) {
        if (Objects.isNull(u)) {
            return null;
        }
        return u.getAcronym();
    }

    @Override
    public UserTypeEnum convertToEntityAttribute(String acronym) {
        return UserTypeEnum.getByAcronym(acronym);
    }
}
