package dev.doel.TDoh.register;

import dev.doel.TDoh.encryptations.EncoderFacade;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserDTO;
import dev.doel.TDoh.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncoderFacade encoderFacade;

    private RegisterService registerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerService = new RegisterService(userRepository, encoderFacade);
    }

    @Test
    void testSaveValidUser() {
        UserDTO userDto = new UserDTO(0, "testuser", "password123", "test@example.com");
        User savedUser = new User(1, "testuser", "test@example.com", "encodedPassword", null);

        when(encoderFacade.decode(eq("base64"), anyString())).thenReturn("decodedPassword");
        when(encoderFacade.encode(eq("bcrypt"), anyString())).thenReturn("encodedPassword");
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = registerService.save(userDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(encoderFacade).decode(eq("base64"), eq("password123"));
        verify(encoderFacade).encode(eq("bcrypt"), eq("decodedPassword"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveWithExistingUsername() {
        UserDTO userDto = new UserDTO(0, "existinguser", "password123", "test@example.com");

        when(encoderFacade.decode(eq("base64"), anyString())).thenReturn("decodedPassword");
        when(encoderFacade.encode(eq("bcrypt"), anyString())).thenReturn("encodedPassword");
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            registerService.save(userDto);
        });

        assertEquals("Username already taken", exception.getMessage());
    }

    @Test
    void testSaveWithExistingEmail() {
        UserDTO userDto = new UserDTO(0, "newuser", "password123", "existing@example.com");

        when(encoderFacade.decode(eq("base64"), anyString())).thenReturn("decodedPassword");
        when(encoderFacade.encode(eq("bcrypt"), anyString())).thenReturn("encodedPassword");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            registerService.save(userDto);
        });

        assertEquals("Email already used", exception.getMessage());
    }
}