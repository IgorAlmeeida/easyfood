package br.com.imsa.easyfood.domain.usercase.usertype;

import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.gateway.UserTypeGateway;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway gateway;

    @InjectMocks
    private SearchUserTypeUseCase useCase;

    @Test
    @DisplayName("execute(pageable) deve delegar para findAll")
    void executeFindAll() {
        Pageable p = PageRequest.of(0, 10);
        Page<UserType> page = new PageImpl<>(List.of(new UserType(1L, "ADMIN")));
        when(gateway.findAll(p)).thenReturn(page);
        Page<UserType> res = useCase.execute(p);
        assertSame(page, res);
    }

    @Test
    @DisplayName("execute(pageable, name) com nome em branco deve chamar findAll; com nome válido chama findByNameContainingIgnoreCase")
    void executeByName() {
        Pageable p = PageRequest.of(0, 5);
        Page<UserType> all = new PageImpl<>(List.of());
        when(gateway.findAll(p)).thenReturn(all);
        assertSame(all, useCase.execute(p, " "));

        Page<UserType> filtered = new PageImpl<>(List.of(new UserType(2L, "CLIENT")));
        when(gateway.findByNameContainingIgnoreCase("CLI", p)).thenReturn(filtered);
        Page<UserType> res = useCase.execute(p, "CLI");
        assertSame(filtered, res);
    }

    @Test
    @DisplayName("findById deve delegar para gateway")
    void findById() {
        when(gateway.findById(3L)).thenReturn(Optional.of(new UserType(3L, "MANAGER")));
        Optional<UserType> out = useCase.findById(3L);
        assertTrue(out.isPresent());
        assertEquals(3L, out.get().getId());
    }
}
