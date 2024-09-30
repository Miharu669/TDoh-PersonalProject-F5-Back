package dev.doel.TDoh.users;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.profiles.ChangePasswordRequest;
import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserService {

    @Autowired
    private UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;
 public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

 
    public void addPoints(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setScore(user.getScore() + points);
        userRepository.save(user);
    }

   
    public void deductPoints(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        int newScore = user.getScore() - points;
        if (newScore < 0) {
            newScore = 0; 
        }
        user.setScore(newScore);
        userRepository.save(user);
    }

    
    public int getUserScore(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getScore();
    }

    
    public void updateUserScore(Long userId, int points) {
        addPoints(userId, points);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
