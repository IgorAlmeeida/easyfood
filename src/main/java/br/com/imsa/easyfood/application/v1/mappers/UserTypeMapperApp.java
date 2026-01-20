package br.com.imsa.easyfood.application.v1.mappers;

import br.com.imsa.easyfood.application.v1.dto.responses.UserTypeResponse;
import br.com.imsa.easyfood.domain.dto.output.usertype.CreateUserTypeOutput;
import br.com.imsa.easyfood.domain.entity.UserType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTypeMapperApp {
    UserTypeResponse toUserTypeResponse(CreateUserTypeOutput userType);
    UserTypeResponse toUserTypeResponse(UserType userType);
}
