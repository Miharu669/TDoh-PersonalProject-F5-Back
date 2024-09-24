package dev.doel.TDoh.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import dev.doel.TDoh.users.User;
import com.google.api.client.json.gson.GsonFactory;
import dev.doel.TDoh.users.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String redirectUri;

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticateUser(String idTokenString) {
        try {
            
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(clientId)) 
                    .build();

            
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                
                String userId = payload.getSubject(); 
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                Optional<User> userOptional = userRepository.findByGoogleId(userId);
                if (userOptional.isEmpty()) {
                    userOptional = userRepository.findByEmail(email);
                }

                User user;
                if (userOptional.isPresent()) {
                    user = userOptional.get();
                } else {
                    user = new User();
                    user.setEmail(email);
                    user.setUsername(name);
                    user.setGoogleId(userId);
                    user.setScore(0); 
                    userRepository.save(user);
                }

                return user;
            } else {
                throw new RuntimeException("Invalid ID Token");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Token verification failed", e);
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("Client ID: " + clientId);
        System.out.println("Redirect URI: " + redirectUri);
    }
}
