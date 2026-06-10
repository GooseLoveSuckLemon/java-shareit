package ru.practicum.shareit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidate_WhenDescriptionIsValid() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need a drill");
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFail_WhenDescriptionIsBlank() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("");
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Описание запроса не может быть пустым");
    }

    @Test
    void shouldFail_WhenDescriptionIsNull() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription(null);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Описание запроса не может быть пустым");
    }
}