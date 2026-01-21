package br.com.imsa.easyfood.domain.usercase.usersystem;

import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserSystemGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUserSystemUseCaseTest {

    @Mock private UserSystemGateway gateway;
    @InjectMocks private SearchUserSystemUseCase useCase;

    private UserSystem user() {
        Address addr = new Address(1L, "Rua", "Bairro", "Cidade", "10", "00000-000");
        UserType type = new UserType(1L, "ADMIN");
        return new UserSystem(7L, "Nome", "a@b.com", "user", "secret1", true, type, addr);
    }

    @Test
    @DisplayName("execute(pageable) deve delegar para findAll")
    void executeFindAll() {
        Pageable p = PageRequest.of(0, 10);
        Page<UserSystem> page = new PageImpl<>(List.of(user()));
        when(gateway.findAll(p)).thenReturn(page);
        assertSame(page, useCase.execute(p));
    }

    @Test
    @DisplayName("execute(pageable, name): branco -> findAll; válido -> findByNameContainingIgnoreCase")
    void executeByName() {
        Pageable p = PageRequest.of(0, 5);
        Page<UserSystem> all = new PageImpl<>(List.of());
        when(gateway.findAll(p)).thenReturn(all);
        assertSame(all, useCase.execute(p, " "));

        Page<UserSystem> filtered = new PageImpl<>(List.of(user()));
        when(gateway.findByNameContainingIgnoreCase("No", p)).thenReturn(filtered);
        assertSame(filtered, useCase.execute(p, "No"));
    }

    @Test
    @DisplayName("findById e findByUsername devem delegar para gateway")
    void finders() {
        when(gateway.findById(7L)).thenReturn(Optional.of(user()));
        when(gateway.findByUsername("user")).thenReturn(Optional.of(user()));
        assertTrue(useCase.findById(7L).isPresent());
        assertTrue(useCase.findByUsername("user").isPresent());
    }
}
