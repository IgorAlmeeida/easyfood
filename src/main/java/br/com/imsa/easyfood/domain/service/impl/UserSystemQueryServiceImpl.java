package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.service.UserSystemQueryService;
import br.com.imsa.easyfood.exception.NegocioException;
import br.com.imsa.easyfood.infrastructure.repository.UserSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSystemQueryServiceImpl implements UserSystemQueryService {

    private final UserSystemRepository userSystemRepository;

    @Override
    public Page<UserSystem> getAllUserSystems(Pageable pageable, String name) {
        return userSystemRepository.findUserSystemByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<UserSystem> getAllUserSystems(Pageable pageable) {
        return userSystemRepository.findAll(pageable);
    }

    @Override
    public UserSystem getUserSystem(Long id) {
        return userSystemRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado."));
    }

    @Override
    public Optional<UserSystem> getUserSystemByUsername(String username) {
        return userSystemRepository.findByUsername(username);
    }
}
