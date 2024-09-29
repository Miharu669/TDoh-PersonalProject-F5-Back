package dev.doel.TDoh.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @SuppressWarnings("unused")
    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
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
