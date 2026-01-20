package br.com.imsa.easyfood.infra.repository;

import br.com.imsa.easyfood.infra.model.UserTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeJpaEntity, Long> {
}
