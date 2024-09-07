package dev.doel.TDoh.users;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUserDto() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .username("validUser")
                .password("validPass")
                .email("valid.email@example.com")
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertTrue(violations.isEmpty(), "Validation errors: " + violations);
    }

    @Test
    public void testInvalidUserDto() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .username("short")
                .password("123")
                .email("invalid-email")
                .build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertEquals(3, violations.size(), "Expected 3 validation errors");

        for (ConstraintViolation<UserDto> violation : violations) {
            String message = violation.getMessage();
            assertTrue(message.contains("Username must be between 6 and 20 characters")
                    || message.contains("Password must be between 6 and 15 characters")
                    || message.contains("Email should be valid"));
        }
    }
}
