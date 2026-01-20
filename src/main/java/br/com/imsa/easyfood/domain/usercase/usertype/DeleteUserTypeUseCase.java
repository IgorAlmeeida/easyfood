package br.com.imsa.easyfood.domain.usercase.usertype;

import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public boolean execute(Long id) {
        return userTypeGateway.findById(id)
                .map(t -> { userTypeGateway.delete(t); return true; })
                .orElse(false);
    }
}
