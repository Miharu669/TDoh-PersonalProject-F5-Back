package dev.doel.TDoh.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserRepository;

import java.io.IOException;

@RestController
@RequestMapping("${api-endpoint}/auth")
@RequiredArgsConstructor
public class AuthController {

    @SuppressWarnings("unused")
    private final UserRepository userRepository;
    private final AuthService authService;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }
}
