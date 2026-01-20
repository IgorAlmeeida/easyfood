package br.com.imsa.easyfood.infra.converter;

import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.domain.enums.UserTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class KichenTypeConverter implements AttributeConverter<KichenTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(KichenTypeEnum u) {
        if (Objects.isNull(u)) {
            return null;
        }
        return u.getAcronym();
    }

    @Override
    public KichenTypeEnum convertToEntityAttribute(String acronym) {
        return KichenTypeEnum.getByAcronym(acronym);
    }
}
