package dev.doel.TDoh.register;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "${api-endpoint}/register")
public class RegisterController {

    private final RegisterService service;

    public RegisterController(RegisterService service) {
        this.service = service;
    }

    @PostMapping
public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserDto newUser) {
    System.out.println("Received RegisterDto: " + newUser);

    User user = service.save(newUser);
    System.out.println("Registered User: " + user);

    Map<String, String> json = new HashMap<>();
    json.put("message", "Register successful");
    json.put("username", user.getUsername());

    return ResponseEntity.status(HttpStatus.CREATED).body(json);
}

}

