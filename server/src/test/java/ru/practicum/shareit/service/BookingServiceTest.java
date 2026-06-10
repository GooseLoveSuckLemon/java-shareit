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
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private Item testItem;
    private Booking testBooking;
    private BookingDto testBookingDto;

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

        testBookingDto = new BookingDto();
        testBookingDto.setId(1L);
        testBookingDto.setStatus(BookingStatus.WAITING);
    }

    @Test
    void createBooking_ShouldSucceed_WhenValid() {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);

        BookingDto result = bookingService.createBooking(2L, requestDto);

        assertThat(result).isNotNull();
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_ShouldThrowException_WhenItemNotAvailable() {
        testItem.setAvailable(false);
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        assertThatThrownBy(() -> bookingService.createBooking(2L, requestDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("недоступна");
    }

    @Test
    void createBooking_ShouldThrowException_WhenOwnerBooksOwnItem() {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        assertThatThrownBy(() -> bookingService.createBooking(1L, requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не может бронировать");
    }

    @Test
    void getBookingById_ShouldReturnBooking_WhenUserIsBooker() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);

        BookingDto result = bookingService.getBookingById(2L, 1L);

        assertThat(result).isNotNull();
    }

    @Test
    void approveBooking_ShouldSucceed_WhenUserIsOwner() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);

        BookingDto result = bookingService.approveBooking(1L, 1L, true);

        assertThat(result).isNotNull();
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void approveBooking_ShouldThrowException_WhenUserIsNotOwner() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        assertThatThrownBy(() -> bookingService.approveBooking(3L, 1L, true))
                .isInstanceOf(ForbiddenException.class);
    }
}