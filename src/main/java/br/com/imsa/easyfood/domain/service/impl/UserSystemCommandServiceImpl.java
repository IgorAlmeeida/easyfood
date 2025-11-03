package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.api.dto.requests.UserSystemCreateRequest;
import br.com.imsa.easyfood.api.dto.requests.UserSystemUpdateRequest;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.service.AddressService;
import br.com.imsa.easyfood.domain.service.UserSystemCommandService;
import br.com.imsa.easyfood.exception.NegocioException;
import br.com.imsa.easyfood.infrastructure.repository.UserSystemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSystemCommandServiceImpl implements UserSystemCommandService {

    private final UserSystemRepository userSystemRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final AddressService addressService;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public UserSystem createUserSystem(UserSystemCreateRequest userSystemCreateRequest) {
        validateEmailHasUser(userSystemCreateRequest.getEmail());

        Address address = addressService.createAddress(userSystemCreateRequest.getAddress());

        UserSystem userSystem = modelMapper.map(userSystemCreateRequest, UserSystem.class);
        userSystem.setPassword(encoder.encode(userSystem.getPassword()));
        userSystem.setAddress(address);
        userSystem.setActive(true);

        userSystemRepository.save(userSystem);
        return userSystem;
    }

    @Override
    @Transactional
    public UserSystem updateUserSystem(Long id, UserSystemUpdateRequest req) {

        UserSystem user = userSystemRepository.findById(id)
                .orElseThrow(() -> new NegocioException(
                        messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));

        if (req.getEmail() != null && !req.getEmail().equals(user.getEmail())) {
            validateEmailHasUser(req.getEmail());
        }

        if (req.getAddress() != null) {
            addressService.updateAddress(user.getAddress(), req.getAddress());
        }

        modelMapper.map(req, user);
        userSystemRepository.save(user);

        return user;
    }


    @Override
    @Transactional
    public void deleteUserSystem(Long id) {
        UserSystem userSystem = userSystemRepository.findById(id)
                .orElseThrow(() -> new NegocioException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));
        userSystemRepository.delete(userSystem);
    }

    private void validateEmailHasUser(String email){
        UserSystem userSystem = userSystemRepository.findUserSystemByEmail(email).orElse(null);
        if (userSystem != null){
            throw new NegocioException(messageSource.getMessage("email.already.registered", null, LocaleContextHolder.getLocale()));
        }
    }
}
