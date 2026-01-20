package br.com.imsa.easyfood.domain.usercase.auth;

import br.com.imsa.easyfood.domain.dto.output.auth.LoginOutput;
import br.com.imsa.easyfood.domain.gateway.AuthGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginUserCase {

    private final AuthGateway authGateway;

    public LoginOutput execute(String username, String password) {
        String token = authGateway.login(username, password);

        return new LoginOutput(token,"Bearer", username, 86400000);

    }
}
