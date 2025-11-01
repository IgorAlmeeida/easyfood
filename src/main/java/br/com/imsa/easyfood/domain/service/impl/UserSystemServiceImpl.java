package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.api.dto.requests.UserSystemRequest;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.service.AddressService;
import br.com.imsa.easyfood.domain.service.UserSystemService;
import br.com.imsa.easyfood.exception.NegocioException;
import br.com.imsa.easyfood.infrastructure.repository.UserSystemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSystemServiceImpl implements UserSystemService, UserDetailsService {

    private final UserSystemRepository userSystemRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private final AddressService addressService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userSystemRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    @Transactional
    public UserSystem createUserSystem(UserSystemRequest userSystemRequest) {

        Address address = addressService.createAddress(userSystemRequest.getAddress());

        UserSystem userSystem = new UserSystem();

        validateEmailHasUser(userSystem.getEmail());

        userSystem = modelMapper.map(userSystemRequest, UserSystem.class);
        userSystem.setPassword(encoder.encode(userSystem.getPassword()));
        userSystem.setAddress(address);

        userSystemRepository.save(userSystem);

        return userSystem;
    }

    @Override
    public Page<UserSystem> getAllUserSystems(Pageable pageable,
                                              String name) {
        return userSystemRepository
                .findUserSystemByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Page<UserSystem> getAllUserSystems(Pageable pageable) {
        return userSystemRepository.findAll(pageable);
    }

    @Override
    public UserSystem getUserSystem(Long id) {
        return userSystemRepository.findById(id)
                .orElse(null);
    }

    @Override
    @Transactional
    public UserSystem updateUserSystem(Long id, UserSystemRequest userSystemRequest) {
        UserSystem oldUserSystem = this.getUserSystem(id);

        if (!userSystemRequest.getEmail().equals(oldUserSystem.getEmail())) {
            validateEmailHasUser(userSystemRequest.getEmail());
        }

        addressService.updateAddress(oldUserSystem.getAddress(), userSystemRequest.getAddress());

        oldUserSystem = modelMapper.map(userSystemRequest, UserSystem.class);
        oldUserSystem.setPassword(encoder.encode(oldUserSystem.getPassword()));

        userSystemRepository.save(oldUserSystem);
        return oldUserSystem;
    }

    @Override
    public void deleteUserSystem(Long id) {
        UserSystem userSystem = this.getUserSystem(id);
        if(userSystem == null){
            throw new NegocioException("Usuário não encontrado.");
        }
        userSystemRepository.delete(userSystem);
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        UserSystem userSystem = this.getUserSystem(id);
        if (!encoder.matches(oldPassword, userSystem.getPassword())){
            throw new NegocioException("Senha atual informada incorreta.");
        }

        userSystem.setPassword(encoder.encode(newPassword));
        userSystemRepository.save(userSystem);

    }

    private void validateEmailHasUser(String email){
        UserSystem userSystem = userSystemRepository.findUserSystemByEmail(email)
                .orElse(null);

        if (userSystem != null){
            throw new NegocioException("Email já cadastrado.");
        }
    }
}
