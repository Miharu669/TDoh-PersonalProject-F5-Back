package dev.doel.TDoh.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserRepository;

@RestController
@RequestMapping("${api-endpoint}/auth") 
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authenticated"));
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged");
        response.put("username", username);
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/login/oauth2/idToken")
    public ResponseEntity<User> authenticateUser(@RequestParam("idToken") String idToken) {
        User user = authService.authenticateUser(idToken);
        return ResponseEntity.ok(user);
    }

    
    @GetMapping("/login/success")
    public ResponseEntity<String> loginSuccess() {
        return ResponseEntity.ok("Autenticación exitosa");
    }

    
    @GetMapping("/login/failure")
    public ResponseEntity<String> loginFailure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticación fallida");
    }
    
}
