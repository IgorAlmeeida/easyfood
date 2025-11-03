package br.com.imsa.easyfood.domain.service.impl;

import br.com.imsa.easyfood.domain.service.UserSystemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserSystemQueryService userSystemService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userSystemService.getUserSystemByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
