package br.com.imsa.easyfood.domain.gateway;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserSystemGateway {

    Optional<UserSystem> findById(Long id);

    Optional<UserSystem> findByUsername(String username);

    Optional<UserSystem> findByEmail(String email);

    Page<UserSystem> findAll(Pageable pageable);

    Page<UserSystem> findByNameContainingIgnoreCase(String name, Pageable pageable);

    UserSystem save(UserSystem userSystem);

    void delete(UserSystem userSystem);
}
