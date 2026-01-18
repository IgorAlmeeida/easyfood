package br.com.imsa.easyfood.infra.repository;

import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSystemRepository extends JpaRepository<UserSystemJpaEntity, Long> {

    Optional<UserSystemJpaEntity> findByUsername(String username);

    Optional<UserSystemJpaEntity> findUserSystemByEmail(String email);

    Page<UserSystemJpaEntity> findUserSystemByNameContainingIgnoreCase(String name, Pageable pageable);
}
