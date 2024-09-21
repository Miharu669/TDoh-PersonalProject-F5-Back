package dev.doel.TDoh.users;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDTOTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenUserNameIsTooShort_thenValidationFails() {
        UserDTO user = UserDTO.builder()
                .username("abc")
                .email("test@example.com")
                .password("password123")
                .build();

        Set<jakarta.validation.ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertTrue(violations.size() > 0);
    }

    @Test
    public void whenEmailIsInvalid_thenValidationFails() {
        UserDTO user = UserDTO.builder()
                .username("validUser")
                .email("invalid-email")
                .password("password123")
                .build();

        Set<jakarta.validation.ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertTrue(violations.size() > 0);
    }

    @Test
    public void whenPasswordIsTooShort_thenValidationFails() {
        UserDTO user = UserDTO.builder()
                .username("validUser")
                .email("test@example.com")
                .password("123")
                .build();

        Set<jakarta.validation.ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertTrue(violations.size() > 0);
    }

    @Test
    public void whenAllFieldsAreValid_thenValidationSucceeds() {
        UserDTO user = UserDTO.builder()
                .username("validUser")
                .email("test@example.com")
                .password("password123")
                .build();

        Set<jakarta.validation.ConstraintViolation<UserDTO>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }
}
