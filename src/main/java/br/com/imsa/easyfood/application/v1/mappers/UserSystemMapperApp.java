package br.com.imsa.easyfood.application.v1.mappers;

import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemUpdateRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.UserSystemResponse;
import br.com.imsa.easyfood.domain.dto.input.usersystem.CreateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.UpdateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.CreateUserSystemOutput;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapperApp.class})
public interface UserSystemMapperApp {

    UserSystemResponse toUserSystemResponse(CreateUserSystemOutput userSystem);

    // Map a domain UserSystem directly to API response
    UserSystemResponse toUserSystemResponse(UserSystem userSystem);

    CreateUserSystemInput  toCreateUserSystemInput(UserSystemCreateRequest userSystem);

    UpdateUserSystemInput toUpdateUserSystemInput(UserSystemUpdateRequest userSystem);
}
