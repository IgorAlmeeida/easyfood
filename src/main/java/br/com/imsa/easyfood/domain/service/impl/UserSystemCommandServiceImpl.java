package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemUpdateRequest;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
import br.com.imsa.easyfood.domain.service.UserSystemCommandService;
import br.com.imsa.easyfood.exception.NegocioException;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.imsa.easyfood.application.usecase.UseCase;
import br.com.imsa.easyfood.infra.mappers.UserSystemMapper;

@UseCase
@RequiredArgsConstructor
public class UserSystemCommandServiceImpl implements UserSystemCommandService {

    private final UserSystemGateway userSystemGateway;
    private final PasswordEncoder encoder;
    private final AddressService addressService;
    private final MessageSource messageSource;
    private final UserSystemMapper userSystemMapper;

    @Override
    @Transactional
    public UserSystemJpaEntity createUserSystem(UserSystemCreateRequest userSystemCreateRequest) {
        validateEmailHasUser(userSystemCreateRequest.getEmail());

        AddressJpaEntity addressJpaEntity = addressService.createAddress(userSystemCreateRequest.getAddress());

        UserSystemJpaEntity userSystemJpaEntity = userSystemMapper.toEntity(userSystemCreateRequest);
        userSystemJpaEntity.setPassword(encoder.encode(userSystemJpaEntity.getPassword()));
        userSystemJpaEntity.setAddressJpaEntity(addressJpaEntity);
        userSystemJpaEntity.setActive(true);

        userSystemGateway.save(userSystemJpaEntity);
        return userSystemJpaEntity;
    }

    @Override
    @Transactional
    public UserSystemJpaEntity updateUserSystem(Long id, UserSystemUpdateRequest req) {

        UserSystemJpaEntity user = userSystemGateway.findById(id)
                .orElseThrow(() -> new NegocioException(
                        messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));

        if (req.getEmail() != null && !req.getEmail().equals(user.getEmail())) {
            validateEmailHasUser(req.getEmail());
        }

        if (req.getAddress() != null) {
            addressService.updateAddress(user.getAddressJpaEntity(), req.getAddress());
        }

        userSystemMapper.update(user, req);
        userSystemGateway.save(user);

        return user;
    }


    @Override
    @Transactional
    public void deleteUserSystem(Long id) {
        UserSystemJpaEntity userSystemJpaEntity = userSystemGateway.findById(id)
                .orElseThrow(() -> new NegocioException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));
        userSystemGateway.delete(userSystemJpaEntity);
    }

    private void validateEmailHasUser(String email){
        UserSystemJpaEntity userSystemJpaEntity = userSystemGateway.findByEmail(email).orElse(null);
        if (userSystemJpaEntity != null){
            throw new NegocioException(messageSource.getMessage("email.already.registered", null, LocaleContextHolder.getLocale()));
        }
    }
}
