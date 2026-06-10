package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionsTest {

    @Test
    void testBadRequestException() {
        BadRequestException exception = new BadRequestException("Bad request message");
        assertThat(exception.getMessage()).isEqualTo("Bad request message");
    }

    @Test
    void testNotFoundException() {
        NotFoundException exception = new NotFoundException("Not found message");
        assertThat(exception.getMessage()).isEqualTo("Not found message");
    }

    @Test
    void testForbiddenException() {
        ForbiddenException exception = new ForbiddenException("Forbidden message");
        assertThat(exception.getMessage()).isEqualTo("Forbidden message");
    }

    @Test
    void testDuplicateEmailException() {
        DuplicateEmailException exception = new DuplicateEmailException("Duplicate email");
        assertThat(exception.getMessage()).isEqualTo("Duplicate email");
    }

    @Test
    void testUnsupportedStateException() {
        UnsupportedStateException exception = new UnsupportedStateException("Unsupported state");
        assertThat(exception.getMessage()).isEqualTo("Unsupported state");
    }
}