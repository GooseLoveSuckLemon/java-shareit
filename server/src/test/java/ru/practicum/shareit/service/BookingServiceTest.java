package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    private User otherUser;
    private Item item;
    private Booking booking;
    private BookingRequestDto requestDto;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");
        owner.setEmail("owner@test.com");

        booker = new User();
        booker.setId(2L);
        booker.setName("Booker");
        booker.setEmail("booker@test.com");

        otherUser = new User();
        otherUser.setId(3L);
        otherUser.setName("Other");
        otherUser.setEmail("other@test.com");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
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
    }

    // ==================== ТЕСТЫ ВАЛИДАЦИИ ДАТ ====================

    @Test
    void createBooking_WhenStartAfterEnd_ShouldThrowException() {
        requestDto.setStart(LocalDateTime.now().plusDays(2));
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void createBooking_WhenStartEqualsEnd_ShouldThrowException() {
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void createBooking_WhenStartInPast_ShouldThrowException() {
        requestDto.setStart(LocalDateTime.now().minusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class);
    }

    // ==================== ТЕСТЫ УСПЕШНОГО СОЗДАНИЯ ====================

    @Test
    void createBooking_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createBooking_WhenItemNotFound_ShouldThrowException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createBooking_WhenOwnerBookOwnItem_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(1L, requestDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createBooking_WhenItemNotAvailable_ShouldThrowException() {
        item.setAvailable(false);
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void createBooking_ShouldReturnBookingDto() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.createBooking(2L, requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    // ==================== ТЕСТЫ ПОДТВЕРЖДЕНИЯ БРОНИРОВАНИЯ ====================

    @Test
    void approveBooking_WhenBookingNotFound_ShouldThrowException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.approveBooking(1L, 1L, true))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void approveBooking_WhenUserNotFound_ShouldThrowException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.approveBooking(1L, 3L, true))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void approveBooking_WhenUserNotOwner_ShouldThrowException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(3L)).thenReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> bookingService.approveBooking(1L, 3L, true))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void approveBooking_WhenAlreadyApproved_ShouldThrowException() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        assertThatThrownBy(() -> bookingService.approveBooking(1L, 1L, true))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void approveBooking_WhenAlreadyRejected_ShouldThrowException() {
        booking.setStatus(BookingStatus.REJECTED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        assertThatThrownBy(() -> bookingService.approveBooking(1L, 1L, true))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void approveBooking_WhenApprove_ShouldSetApprovedStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.approveBooking(1L, 1L, true);

        assertThat(result).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void approveBooking_WhenReject_ShouldSetRejectedStatus() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.approveBooking(1L, 1L, false);

        assertThat(result).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    // ==================== ТЕСТЫ ПОЛУЧЕНИЯ БРОНИРОВАНИЯ ПО ID ====================

    @Test
    void getBookingById_WhenBookingNotFound_ShouldThrowException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingById(1L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getBookingById_WhenUserNotFound_ShouldThrowException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingById(1L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getBookingById_WhenUserNotBookerOrOwner_ShouldThrowException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(3L)).thenReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> bookingService.getBookingById(1L, 3L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getBookingById_WhenUserIsOwner_ShouldReturnBookingDto() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    // ==================== ТЕСТЫ ПОЛУЧЕНИЯ СПИСКОВ БРОНИРОВАНИЙ ====================

    @Test
    void getBookingsByBooker_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingsByBooker(2L, BookingState.ALL))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getBookingsByBooker_WithStateAll_ShouldReturnBookings() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerId(2L)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByBooker(2L, BookingState.ALL);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void getBookingsByBooker_WithEmptyList_ShouldReturnEmpty() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBookerId(2L)).thenReturn(Collections.emptyList());

        var result = bookingService.getBookingsByBooker(2L, BookingState.ALL);

        assertThat(result).isEmpty();
    }

    @Test
    void getBookingsByOwner_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingsByOwner(1L, BookingState.ALL))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getBookingsByOwner_WithStateAll_ShouldReturnBookings() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByOwnerId(1L)).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByOwner(1L, BookingState.ALL);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void getBookingsByOwner_WithEmptyList_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByOwnerId(1L)).thenReturn(Collections.emptyList());

        var result = bookingService.getBookingsByOwner(1L, BookingState.ALL);

        assertThat(result).isEmpty();
    }
}