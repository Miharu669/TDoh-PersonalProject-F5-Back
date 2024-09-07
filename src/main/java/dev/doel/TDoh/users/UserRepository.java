package dev.doel.TDoh.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    
}
