package dev.doel.TDoh.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.users.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    UserRepository repository;

    public JpaUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return repository
                .findByUsername(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

}
