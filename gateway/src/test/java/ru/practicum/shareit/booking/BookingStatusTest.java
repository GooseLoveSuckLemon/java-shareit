package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.BookingStatus;

import static org.assertj.core.api.Assertions.assertThat;

class BookingStatusTest {

    @Test
    void testEnumValues() {
        assertThat(BookingStatus.WAITING).isNotNull();
        assertThat(BookingStatus.APPROVED).isNotNull();
        assertThat(BookingStatus.REJECTED).isNotNull();
        assertThat(BookingStatus.CANCELED).isNotNull();
    }

    @Test
    void testEnumNames() {
        assertThat(BookingStatus.WAITING.name()).isEqualTo("WAITING");
        assertThat(BookingStatus.APPROVED.name()).isEqualTo("APPROVED");
        assertThat(BookingStatus.REJECTED.name()).isEqualTo("REJECTED");
        assertThat(BookingStatus.CANCELED.name()).isEqualTo("CANCELED");
    }
}