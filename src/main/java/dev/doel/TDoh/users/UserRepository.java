package dev.doel.TDoh.users;

import java.util.Optional;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    // Optional<User> findByGoogleId(String googleId);
    boolean existsByEmail(String email);
    List<User> findByScoreGreaterThan(int score);
 
}
