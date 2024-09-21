package dev.doel.TDoh.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    
}
