package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleBadRequest_WithMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("Validation failed");

        Map<String, String> result = errorHandler.handleBadRequest(exception);

        assertThat(result).containsKey("error");
        assertThat(result.get("error")).isEqualTo("Validation failed");
    }

    @Test
    void handleNotFound_ShouldReturnNotFoundMessage() {
        NotFoundException exception = new NotFoundException("User not found with id: 999");

        Map<String, String> result = errorHandler.handleNotFound(exception);

        assertThat(result).containsEntry("error", "User not found with id: 999");
    }

    @Test
    void handleForbidden_ShouldReturnForbiddenMessage() {
        ForbiddenException exception = new ForbiddenException("Access denied");

        Map<String, String> result = errorHandler.handleForbidden(exception);

        assertThat(result).containsEntry("error", "Access denied");
    }

    @Test
    void handleDuplicateEmail_ShouldReturnConflictMessage() {
        DuplicateEmailException exception = new DuplicateEmailException("Email already exists: test@example.com");

        Map<String, String> result = errorHandler.handleDuplicateEmail(exception);

        assertThat(result).containsEntry("error", "Email already exists: test@example.com");
    }

    @Test
    void handleUnsupportedState_ShouldReturnBadRequestMessage() {
        UnsupportedStateException exception = new UnsupportedStateException("Unknown state: INVALID");

        Map<String, String> result = errorHandler.handleUnsupportedState(exception);

        assertThat(result).containsEntry("error", "Unknown state: INVALID");
    }

    @Test
    void handleGeneric_ShouldReturnInternalServerError() {
        Exception exception = new RuntimeException("Unexpected error");

        Map<String, String> result = errorHandler.handleGeneric(exception);

        assertThat(result).containsEntry("error", "Internal server error");
    }
}
