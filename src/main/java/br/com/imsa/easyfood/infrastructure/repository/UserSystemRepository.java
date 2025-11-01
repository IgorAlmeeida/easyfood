package br.com.imsa.easyfood.infrastructure.repository;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSystemRepository extends JpaRepository<UserSystem, Long> {

    Optional<UserSystem> findByUsername(String username);

    Optional<UserSystem> findUserSystemByEmail(String email);

    Page<UserSystem> findUserSystemByNameContainingIgnoreCase(String name, Pageable pageable);
}
