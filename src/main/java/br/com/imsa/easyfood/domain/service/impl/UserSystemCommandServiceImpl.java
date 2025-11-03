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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSystemCommandServiceImpl implements UserSystemCommandService {

    private final UserSystemRepository userSystemRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final AddressService addressService;

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
    public UserSystem updateUserSystem(Long id, UserSystemUpdateRequest userSystemUpdateRequest) {
        UserSystem oldUserSystem = userSystemRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado."));

        if (!userSystemUpdateRequest.getEmail().equals(oldUserSystem.getEmail())) {
            validateEmailHasUser(userSystemUpdateRequest.getEmail());
        }

        // Atualiza endereço
        addressService.updateAddress(oldUserSystem.getAddress(), userSystemUpdateRequest.getAddress());

        // Recria (ou mapeia) a entidade preservando o id
        oldUserSystem = modelMapper.map(userSystemUpdateRequest, UserSystem.class);
        oldUserSystem.setId(id);
        oldUserSystem.setPassword(encoder.encode(oldUserSystem.getPassword()));

        userSystemRepository.save(oldUserSystem);
        return oldUserSystem;
    }

    @Override
    @Transactional
    public void deleteUserSystem(Long id) {
        UserSystem userSystem = userSystemRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado."));
        userSystemRepository.delete(userSystem);
    }

    private void validateEmailHasUser(String email){
        UserSystem userSystem = userSystemRepository.findUserSystemByEmail(email).orElse(null);
        if (userSystem != null){
            throw new NegocioException("Email já cadastrado.");
        }
    }
}
