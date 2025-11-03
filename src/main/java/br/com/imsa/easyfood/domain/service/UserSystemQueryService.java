package br.com.imsa.easyfood.domain.service;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserSystemQueryService {

    Page<UserSystem> getAllUserSystems(Pageable pageable,
                                       String name);

    Page<UserSystem> getAllUserSystems(Pageable pageable);

    UserSystem getUserSystem(Long id);

    Optional<UserSystem> getUserSystemByUsername(String username);
}
