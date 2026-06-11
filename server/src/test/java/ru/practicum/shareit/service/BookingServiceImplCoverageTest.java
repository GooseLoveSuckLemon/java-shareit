package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplCoverageTest {

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
    private Item item;
    private Booking booking;
    private BookingRequestDto requestDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");

        booker = new User();
        booker.setId(2L);
        booker.setName("Booker");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setAvailable(true);
        item.setOwner(1L);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void createBooking_WhenDatesEqual_ShouldThrowException() {
        // Моки не нужны - валидация дат происходит первой
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("должна быть позже");
    }

    @Test
    void createBooking_WhenStartInPast_ShouldThrowException() {
        requestDto.setStart(LocalDateTime.now().minusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("не может быть в прошлом");
    }

    @Test
    void getBookingsByBooker_WithEmptyState_ShouldReturnEmpty() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerId(2L)).thenReturn(Collections.emptyList());

        var result = bookingService.getBookingsByBooker(2L, BookingState.ALL);

        assertThat(result).isEmpty();
    }

    @Test
    void getBookingsByOwner_WithEmptyState_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByOwnerId(1L)).thenReturn(Collections.emptyList());

        var result = bookingService.getBookingsByOwner(1L, BookingState.ALL);

        assertThat(result).isEmpty();
    }

    @Test
    void approveBooking_WhenAlreadyApproved_ShouldThrowException() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        assertThatThrownBy(() -> bookingService.approveBooking(1L, 1L, true))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("уже имеет статус");
    }

    @Test
    void approveBooking_WhenRejected_ShouldSetRejectedStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(new BookingDto());

        BookingDto result = bookingService.approveBooking(1L, 1L, false);

        assertThat(result).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.REJECTED);
    }
}