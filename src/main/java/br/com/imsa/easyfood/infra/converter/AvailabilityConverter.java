package br.com.imsa.easyfood.infra.converter;

import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import br.com.imsa.easyfood.domain.enums.UserTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class AvailabilityConverter implements AttributeConverter<AvailabilityEnum, String> {

    @Override
    public String convertToDatabaseColumn(AvailabilityEnum u) {
        if (Objects.isNull(u)) {
            return null;
        }
        return u.getAcronym();
    }

    @Override
    public AvailabilityEnum convertToEntityAttribute(String acronym) {
        return AvailabilityEnum.getByAcronym(acronym);
    }
}
