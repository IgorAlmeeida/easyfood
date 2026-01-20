package br.com.imsa.easyfood.domain.usercase.usertype;

import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class SearchUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public Page<UserType> execute(Pageable pageable) {
        return userTypeGateway.findAll(pageable);
    }

    public Page<UserType> execute(Pageable pageable, String name) {
        if (name == null || name.isBlank()) {
            return userTypeGateway.findAll(pageable);
        }
        return userTypeGateway.findByNameContainingIgnoreCase(name, pageable);
    }

    public Optional<UserType> findById(Long id) {
        return userTypeGateway.findById(id);
    }
}
