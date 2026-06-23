package ru.practicum.shareit.entity;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    @Test
    void testGettersAndSetters() {
        User booker = new User();
        booker.setId(1L);

        Item item = new Item();
        item.setId(1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);

        assertThat(booking.getId()).isEqualTo(1L);
        assertThat(booking.getStart()).isNotNull();
        assertThat(booking.getEnd()).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(booking.getBooker()).isEqualTo(booker);
        assertThat(booking.getItem()).isEqualTo(item);
    }

    @Test
    void testAllArgsConstructor() {
        User booker = new User();
        Item item = new Item();
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                item, booker, BookingStatus.WAITING);
        assertThat(booking.getId()).isEqualTo(1L);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testNoArgsConstructor() {
        Booking booking = new Booking();
        assertThat(booking).isNotNull();
    }
}