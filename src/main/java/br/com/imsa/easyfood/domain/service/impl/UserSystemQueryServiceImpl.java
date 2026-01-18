package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import br.com.imsa.easyfood.domain.service.UserSystemQueryService;
import br.com.imsa.easyfood.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.imsa.easyfood.application.usecase.UseCase;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class UserSystemQueryServiceImpl implements UserSystemQueryService {

    private final UserSystemGateway userSystemGateway;
    private final MessageSource messageSource;

    @Override
    public Page<UserSystemJpaEntity> getAllUserSystems(Pageable pageable, String name) {
        return userSystemGateway.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<UserSystemJpaEntity> getAllUserSystems(Pageable pageable) {
        return userSystemGateway.findAll(pageable);
    }

    @Override
    public UserSystemJpaEntity getUserSystem(Long id) {
        return userSystemGateway.findById(id)
                .orElseThrow(() -> new NegocioException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));
    }

    @Override
    public Optional<UserSystemJpaEntity> getUserSystemByUsername(String username) {
        return userSystemGateway.findByUsername(username);
    }
}
