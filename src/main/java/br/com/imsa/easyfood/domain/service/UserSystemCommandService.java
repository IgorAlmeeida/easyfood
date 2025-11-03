package br.com.imsa.easyfood.domain.service;

import br.com.imsa.easyfood.api.dto.requests.UserSystemCreateRequest;
import br.com.imsa.easyfood.api.dto.requests.UserSystemUpdateRequest;
import br.com.imsa.easyfood.domain.entity.UserSystem;

public interface UserSystemCommandService {

    UserSystem createUserSystem(UserSystemCreateRequest userSystemCreateRequest);

    UserSystem updateUserSystem(Long id,
                                UserSystemUpdateRequest userSystemCreateRequest);

    void deleteUserSystem(Long id);
}
