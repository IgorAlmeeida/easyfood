package br.com.imsa.easyfood.domain.service;


import br.com.imsa.easyfood.api.dto.requests.UserSystemRequest;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSystemService {

    UserSystem createUserSystem(UserSystemRequest userSystemRequest);

    Page<UserSystem> getAllUserSystems(Pageable pageable,
                                       String name);

    Page<UserSystem> getAllUserSystems(Pageable pageable);

    UserSystem getUserSystem(Long id);

    UserSystem updateUserSystem(Long id,
                                UserSystemRequest userSystemRequest);

    void deleteUserSystem(Long id);

    void changePassword(Long id, String oldPassword, String newPassword);

}
