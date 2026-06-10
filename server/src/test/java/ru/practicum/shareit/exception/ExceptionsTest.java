package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionsTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

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

    @Test
    void testBadRequestExceptionConstructor() {
        BadRequestException exception = new BadRequestException("Test message");
        assertThat(exception.getMessage()).isEqualTo("Test message");
    }

    @Test
    void testNotFoundExceptionConstructor() {
        NotFoundException exception = new NotFoundException("Not found");
        assertThat(exception.getMessage()).isEqualTo("Not found");
    }

    @Test
    void testForbiddenExceptionConstructor() {
        ForbiddenException exception = new ForbiddenException("Forbidden");
        assertThat(exception.getMessage()).isEqualTo("Forbidden");
    }

    @Test
    void testDuplicateEmailExceptionConstructor() {
        DuplicateEmailException exception = new DuplicateEmailException("Duplicate email");
        assertThat(exception.getMessage()).isEqualTo("Duplicate email");
    }

    @Test
    void testUnsupportedStateExceptionConstructor() {
        UnsupportedStateException exception = new UnsupportedStateException("Unsupported state");
        assertThat(exception.getMessage()).isEqualTo("Unsupported state");
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("Validation error");

        Map<String, String> result = errorHandler.handleBadRequest(exception);

        assertThat(result).containsKey("error");
    }

    @Test
    void testHandleMethodArgumentTypeMismatch() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getValue()).thenReturn("invalid_value");

        Map<String, String> result = errorHandler.handleTypeMismatch(exception);

        assertThat(result).containsEntry("error", "Unknown state: invalid_value");
    }
}