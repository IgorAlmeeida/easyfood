package br.com.imsa.easyfood.domain.service;

import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemUpdateRequest;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;

public interface UserSystemCommandService {

    UserSystemJpaEntity createUserSystem(UserSystemCreateRequest userSystemCreateRequest);

    UserSystemJpaEntity updateUserSystem(Long id,
                                         UserSystemUpdateRequest userSystemCreateRequest);

    void deleteUserSystem(Long id);
}
