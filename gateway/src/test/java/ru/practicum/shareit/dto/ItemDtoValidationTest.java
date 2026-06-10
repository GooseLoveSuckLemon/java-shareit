package ru.practicum.shareit.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ItemDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidate_WhenAllFieldsValid() {
        ItemDto dto = new ItemDto(null, "Drill", "Powerful drill", true, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFail_WhenNameIsBlank() {
        ItemDto dto = new ItemDto(null, "", "Powerful drill", true, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Название не должно быть пустым");
    }

    @Test
    void shouldFail_WhenDescriptionIsBlank() {
        ItemDto dto = new ItemDto(null, "Drill", "", true, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Описание не должно быть пустым");
    }

    @Test
    void shouldFail_WhenAvailableIsNull() {
        ItemDto dto = new ItemDto(null, "Drill", "Powerful drill", null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Статус доступности не должен быть пустым");
    }
}