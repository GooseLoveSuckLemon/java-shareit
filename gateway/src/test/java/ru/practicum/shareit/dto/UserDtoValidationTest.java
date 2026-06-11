package ru.practicum.shareit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidate_WhenAllFieldsValid() {
        UserDto dto = new UserDto(null, "John Doe", "john@example.com");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFail_WhenNameIsBlank() {
        UserDto dto = new UserDto(null, "", "john@example.com");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Имя не должно быть пустым");
    }

    @Test
    void shouldFail_WhenEmailIsBlank() {
        UserDto dto = new UserDto(null, "John Doe", "");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Почта не должна быть пустой");
    }

    @Test
    void shouldFail_WhenEmailIsInvalid() {
        UserDto dto = new UserDto(null, "John Doe", "invalid-email");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Неверный формат почты");
    }
}