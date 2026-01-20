package br.com.imsa.easyfood.domain.usercase.usertype;

import br.com.imsa.easyfood.domain.dto.input.usertype.CreateUserTypeInput;
import br.com.imsa.easyfood.domain.dto.output.usertype.CreateUserTypeOutput;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class CreateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public Optional<CreateUserTypeOutput> execute(CreateUserTypeInput input) {
        if (input == null) return Optional.empty();
        UserType t = new UserType(input.name());
        UserType saved = userTypeGateway.save(t);
        if (saved == null) return Optional.empty();
        return Optional.of(new CreateUserTypeOutput(saved.getId(), saved.getName()));
    }
}
