package ru.practicum.shareit.booking;

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
    void testFromString_WithValidValues() {
        assertThat(BookingState.from("ALL")).contains(BookingState.ALL);
        assertThat(BookingState.from("CURRENT")).contains(BookingState.CURRENT);
        assertThat(BookingState.from("FUTURE")).contains(BookingState.FUTURE);
        assertThat(BookingState.from("PAST")).contains(BookingState.PAST);
        assertThat(BookingState.from("REJECTED")).contains(BookingState.REJECTED);
        assertThat(BookingState.from("WAITING")).contains(BookingState.WAITING);
    }

    @Test
    void testFromString_WithLowerCase() {
        assertThat(BookingState.from("all")).contains(BookingState.ALL);
        assertThat(BookingState.from("current")).contains(BookingState.CURRENT);
        assertThat(BookingState.from("future")).contains(BookingState.FUTURE);
    }

    @Test
    void testFromString_WithInvalidValue() {
        assertThat(BookingState.from("invalid")).isEmpty();
    }

    @Test
    void testFromString_WithNull() {
        assertThat(BookingState.from(null)).isEmpty();
    }
}