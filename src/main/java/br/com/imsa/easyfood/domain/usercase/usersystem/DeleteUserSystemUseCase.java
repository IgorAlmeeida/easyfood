package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteUserSystemUseCase {

    private final UserSystemGateway userSystemGateway;

    public boolean execute(Long id) {
        return userSystemGateway.findById(id)
                .map(user -> {
                    userSystemGateway.delete(user);
                    return true;
                })
                .orElse(false);
    }
}
