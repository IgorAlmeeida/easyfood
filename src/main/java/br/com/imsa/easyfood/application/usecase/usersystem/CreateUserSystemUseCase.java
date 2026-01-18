package br.com.imsa.easyfood.application.usecase.usersystem;

import br.com.imsa.easyfood.domain.dto.input.usersystem.CreateUserSystemInput;

public interface CreateUserSystemUseCase {
    UserSystemOutput execute(CreateUserSystemInput input);
}
