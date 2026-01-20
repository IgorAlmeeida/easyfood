package br.com.imsa.easyfood.domain.gateway;

import br.com.imsa.easyfood.domain.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserTypeGateway {

    Optional<UserType> findById(Long id);

    Page<UserType> findAll(Pageable pageable);

    Page<UserType> findByNameContainingIgnoreCase(String name, Pageable pageable);

    UserType save(UserType userType);

    void delete(UserType userType);
}
