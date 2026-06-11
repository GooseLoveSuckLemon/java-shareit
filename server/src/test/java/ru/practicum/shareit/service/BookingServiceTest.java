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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

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
    private BookingDto bookingDto;

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

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus(BookingStatus.WAITING);
    }

    @Test
    void createBooking_ShouldSucceed() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.createBooking(2L, requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void createBooking_WhenItemNotAvailable_ShouldThrowException() {
        item.setAvailable(false);
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("недоступна");
    }

    @Test
    void createBooking_WhenOwnerBooksOwnItem_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(1L, requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не может бронировать");
    }

    @Test
    void createBooking_WhenStartEqualsEnd_ShouldThrowException() {
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        // Не нужно стейблить репозитории, так как проверка даты происходит первой
        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("должна быть позже");
    }

    @Test
    void createBooking_WhenStartAfterEnd_ShouldThrowException() {
        requestDto.setStart(LocalDateTime.now().plusDays(2));
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        // Удалите стейблинги userRepository и itemRepository
        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("должна быть позже");
    }

    @Test
    void createBooking_WhenStartInPast_ShouldThrowException() {
        requestDto.setStart(LocalDateTime.now().minusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        // Удалите стейблинги userRepository и itemRepository
        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("не может быть в прошлом");
    }

    @Test
    void approveBooking_ShouldApprove() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.approveBooking(1L, 1L, true);

        assertThat(result).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void approveBooking_ShouldReject() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.approveBooking(1L, 1L, false);

        assertThat(result).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.REJECTED);
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
    void getBookingById_AsBooker_ShouldReturnBooking() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(2L, 1L);

        assertThat(result).isNotNull();
    }

    @Test
    void getBookingById_AsOwner_ShouldReturnBooking() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(1L, 1L);

        assertThat(result).isNotNull();
    }

    @Test
    void getBookingById_AsUnauthorized_ShouldThrowException() {
        when(userRepository.findById(3L)).thenReturn(Optional.of(new User()));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.getBookingById(3L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("нет прав");
    }

    @Test
    void getBookingsByBooker_WithAllState_ShouldReturnAll() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerId(2L)).thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByBooker(2L, BookingState.ALL);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithCurrentState_ShouldReturnCurrent() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(any(), any(), any()))
                .thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByBooker(2L, BookingState.CURRENT);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithPastState_ShouldReturnPast() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndEndBefore(any(), any()))
                .thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByBooker(2L, BookingState.PAST);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithFutureState_ShouldReturnFuture() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStartAfter(any(), any()))
                .thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByBooker(2L, BookingState.FUTURE);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WithWaitingState_ShouldReturnWaiting() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerIdAndStatus(2L, BookingStatus.WAITING))
                .thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

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

    @Test
    void getBookingsByBooker_WithInvalidState_ShouldThrowException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));

        assertThatThrownBy(() -> bookingService.getBookingsByBooker(2L, null))
                .isInstanceOf(NullPointerException.class); // Изменено с IllegalArgumentException
    }

    @Test
    void getBookingsByOwner_WithAllState_ShouldReturnAll() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByOwnerId(1L)).thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByOwner(1L, BookingState.ALL);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByOwner_WithCurrentState_ShouldReturnCurrent() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        // Исправлено: используем any() вместо точного LocalDateTime.now()
        when(bookingRepository.findCurrentByOwnerId(eq(1L), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByOwner(1L, BookingState.CURRENT);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByOwner_WithPastState_ShouldReturnPast() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        // Исправлено: используем any() вместо точного LocalDateTime.now()
        when(bookingRepository.findPastByOwnerId(eq(1L), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByOwner(1L, BookingState.PAST);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByOwner_WithFutureState_ShouldReturnFuture() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        // Исправлено: используем any() вместо точного LocalDateTime.now()
        when(bookingRepository.findFutureByOwnerId(eq(1L), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByOwner(1L, BookingState.FUTURE);

        assertThat(result).hasSize(1);
    }

    @Test
    void getBookingsByBooker_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingsByBooker(99L, BookingState.ALL))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getBookingsByOwner_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingsByOwner(99L, BookingState.ALL))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getBookingById_WhenBookingNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingById(1L, 99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найдено");
    }
}