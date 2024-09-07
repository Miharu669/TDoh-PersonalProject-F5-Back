package dev.doel.TDoh.register;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserDto;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RegisterControllerTest {

    @Mock
    private RegisterService service;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterSuccess() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password123");

        User user = new User();
        user.setUsername("testuser");

        when(service.save(userDto)).thenReturn(user);

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Register successful");
        expectedResponse.put("username", "testuser");

        ResponseEntity<Map<String, String>> response = registerController.register(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}
