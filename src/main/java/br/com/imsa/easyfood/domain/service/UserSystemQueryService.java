package br.com.imsa.easyfood.domain.service;

import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserSystemQueryService {

    Page<UserSystemJpaEntity> getAllUserSystems(Pageable pageable,
                                                String name);

    Page<UserSystemJpaEntity> getAllUserSystems(Pageable pageable);

    UserSystemJpaEntity getUserSystem(Long id);

    Optional<UserSystemJpaEntity> getUserSystemByUsername(String username);
}
