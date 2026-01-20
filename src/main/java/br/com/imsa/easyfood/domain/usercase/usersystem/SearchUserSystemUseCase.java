package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class SearchUserSystemUseCase {

    private final UserSystemGateway userSystemGateway;

    public Page<UserSystem> execute(Pageable pageable) {
        return userSystemGateway.findAll(pageable);
    }

    public Page<UserSystem> execute(Pageable pageable, String name) {
        if (name == null || name.isBlank()) {
            return userSystemGateway.findAll(pageable);
        }
        return userSystemGateway.findByNameContainingIgnoreCase(name, pageable);
    }

    public Optional<UserSystem> findById(Long id) {
        return userSystemGateway.findById(id);
    }

    public Optional<UserSystem> findByUsername(String username) {
        return userSystemGateway.findByUsername(username);
    }
}
