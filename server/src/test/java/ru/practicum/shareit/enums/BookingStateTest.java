package ru.practicum.shareit.enums;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingState;

import static org.assertj.core.api.Assertions.assertThat;

class BookingStateTest {

    @Test
    void testEnumValues() {
        assertThat(BookingState.ALL).isNotNull();
        assertThat(BookingState.CURRENT).isNotNull();
        assertThat(BookingState.FUTURE).isNotNull();
        assertThat(BookingState.PAST).isNotNull();
        assertThat(BookingState.REJECTED).isNotNull();
        assertThat(BookingState.WAITING).isNotNull();
    }

    @Test
    void testEnumNames() {
        assertThat(BookingState.ALL.name()).isEqualTo("ALL");
        assertThat(BookingState.CURRENT.name()).isEqualTo("CURRENT");
        assertThat(BookingState.FUTURE.name()).isEqualTo("FUTURE");
        assertThat(BookingState.PAST.name()).isEqualTo("PAST");
        assertThat(BookingState.REJECTED.name()).isEqualTo("REJECTED");
        assertThat(BookingState.WAITING.name()).isEqualTo("WAITING");
    }
}