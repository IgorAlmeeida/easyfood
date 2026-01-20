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

    public boolean execute(Long userId, String oldPassword, String newPassword) {
        Optional<UserSystem> opt = userSystemGateway.findById(userId);
        if (opt.isEmpty()) return false;
        UserSystem user = opt.get();
        // Minimal change: set new password; validation/encoding can be handled at gateway or repository level
        user.setPassword(newPassword);
        userSystemGateway.save(user);
        return true;
    }
}
