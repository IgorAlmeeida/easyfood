package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.service.UserSystemPasswordService;
import br.com.imsa.easyfood.exception.NegocioException;
import br.com.imsa.easyfood.infrastructure.repository.UserSystemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSystemPasswordServiceImpl implements UserSystemPasswordService {

    private final UserSystemRepository userSystemRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        UserSystem userSystem = userSystemRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado."));

        if (!encoder.matches(oldPassword, userSystem.getPassword())){
            throw new NegocioException("Senha atual informada incorreta.");
        }

        userSystem.setPassword(encoder.encode(newPassword));
        userSystemRepository.save(userSystem);
    }
}
