package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import br.com.imsa.easyfood.domain.service.UserSystemPasswordService;
import br.com.imsa.easyfood.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.imsa.easyfood.application.usecase.UseCase;

@UseCase
@RequiredArgsConstructor
public class UserSystemPasswordServiceImpl implements UserSystemPasswordService {

    private final UserSystemGateway userSystemGateway;
    private final PasswordEncoder encoder;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        UserSystemJpaEntity userSystemJpaEntity = userSystemGateway.findById(id)
                .orElseThrow(() -> new NegocioException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));

        if (!encoder.matches(oldPassword, userSystemJpaEntity.getPassword())){
            throw new NegocioException(messageSource.getMessage("password.current.incorrect", null, LocaleContextHolder.getLocale()));
        }

        userSystemJpaEntity.setPassword(encoder.encode(newPassword));
        userSystemGateway.save(userSystemJpaEntity);
    }
}
