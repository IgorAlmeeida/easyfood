package br.com.imsa.easyfood.domain.usercase.auth;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class ChangePasswordUserCase {

    public final UserSystemGateway userSystemGateway;

    public boolean execute(Long userId,
                           String oldPassword,
                           String newPassword) {

        Optional<UserSystem> opt = userSystemGateway.findById(userId);
        if (opt.isEmpty()){
            return false;
        }

        //Todo adicionar matcher senha antiga com a senha atual
        UserSystem user = opt.get();

        UserSystem updated = new UserSystem(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                newPassword,
                user.isActive(),
                user.getUserType(),
                user.getAddress()
        );

        userSystemGateway.save(updated);

        return true;
    }
}
