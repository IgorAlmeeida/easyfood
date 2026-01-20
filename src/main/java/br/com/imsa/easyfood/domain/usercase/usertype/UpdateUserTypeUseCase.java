package br.com.imsa.easyfood.domain.usercase.usertype;

import br.com.imsa.easyfood.domain.dto.input.usertype.UpdateUserTypeInput;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class UpdateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public Optional<UserType> execute(UpdateUserTypeInput input) {
        if (input == null || input.id() == null) return Optional.empty();
        Optional<UserType> opt = userTypeGateway.findById(input.id());
        if (opt.isEmpty()) return Optional.empty();
        UserType current = opt.get();
        String name = input.name() != null ? input.name() : current.getName();
        UserType merged = new UserType(current.getId(), name);
        UserType saved = userTypeGateway.save(merged);
        return Optional.ofNullable(saved);
    }
}
