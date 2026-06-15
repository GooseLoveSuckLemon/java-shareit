package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private User booker;
    private Item testItem;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");

        booker = new User();
        booker.setId(2L);
        booker.setName("Booker");

        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Test Item");
        testItem.setAvailable(true);
        testItem.setOwner(1L);

        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setStart(LocalDateTime.now().plusDays(1));
        testBooking.setEnd(LocalDateTime.now().plusDays(2));
        testBooking.setItem(testItem);
        testBooking.setBooker(booker);
        testBooking.setStatus(BookingStatus.WAITING);
    }

    @Test
    void getBookingsByBooker_WithAllState_ShouldReturnAll() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerId(2L)).thenReturn(Collections.singletonList(testBooking));
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(new BookingDto());

        var result = bookingService.getBookingsByBooker(2L, BookingState.ALL);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithCurrentState_ShouldReturnCurrent() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(any(), any(), any()))
                .thenReturn(Collections.singletonList(testBooking));
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(new BookingDto());

        var result = bookingService.getBookingsByBooker(2L, BookingState.CURRENT);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithPastState_ShouldReturnPast() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndEndBefore(any(), any()))
                .thenReturn(Collections.singletonList(testBooking));
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(new BookingDto());

        var result = bookingService.getBookingsByBooker(2L, BookingState.PAST);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithFutureState_ShouldReturnFuture() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStartAfter(any(), any()))
                .thenReturn(Collections.singletonList(testBooking));
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(new BookingDto());

        var result = bookingService.getBookingsByBooker(2L, BookingState.FUTURE);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithWaitingState_ShouldReturnWaiting() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStatus(2L, BookingStatus.WAITING))
                .thenReturn(Collections.singletonList(testBooking));
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(new BookingDto());

        var result = bookingService.getBookingsByBooker(2L, BookingState.WAITING);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithRejectedState_ShouldReturnRejected() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStatus(2L, BookingStatus.REJECTED))
                .thenReturn(Collections.emptyList());

        var result = bookingService.getBookingsByBooker(2L, BookingState.REJECTED);

        assertThat(result).isEmpty();
    }
}