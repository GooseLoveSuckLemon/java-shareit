package ru.practicum.shareit;

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
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShareItTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User testUser;
    private UserDto testUserDto;
    private Item testItem;
    private ItemDto testItemDto;
    private Booking testBooking;
    private BookingDto testBookingDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");

        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setName("John Doe");
        testUserDto.setEmail("john@example.com");

        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setAvailable(true);
        testItem.setOwner(1L);

        testItemDto = new ItemDto();
        testItemDto.setId(1L);
        testItemDto.setName("Test Item");
        testItemDto.setDescription("Test Description");
        testItemDto.setAvailable(true);

        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setStart(LocalDateTime.now().plusDays(1));
        testBooking.setEnd(LocalDateTime.now().plusDays(2));
        testBooking.setItem(testItem);
        testBooking.setBooker(testUser);
        testBooking.setStatus(BookingStatus.WAITING);

        testBookingDto = new BookingDto();
        testBookingDto.setId(1L);
        testBookingDto.setStatus(BookingStatus.WAITING);
    }

    // USER TESTS

    @Test
    void testCreateUser_Success() {
        when(userRepository.findByEmail(testUserDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUserModel(testUserDto)).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.createUser(testUserDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateEmail_ThrowsException() {
        when(userRepository.findByEmail(testUserDto.getEmail())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.createUser(testUserDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("уже существует");
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testGetUserById_NotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void testUpdateUser_Success() {
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toUserDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.updateUser(updateDto, 1L);

        assertThat(result).isNotNull();
        verify(userRepository).save(testUser);
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound_ThrowsException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не найден");
    }

    // ITEM TESTS

    @Test
    void testCreateItem_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(itemMapper.toItemModel(any(ItemDto.class), eq(1L), any())).thenReturn(testItem);
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        ItemDto result = itemService.createItem(1L, testItemDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Item");
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void testCreateItem_UserNotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.createItem(99L, testItemDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void testGetItemById_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        ItemDto result = itemService.getItemById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testGetItemById_NotFound_ThrowsException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не найдена");
    }

    @Test
    void testUpdateItem_Success() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Item");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        ItemDto result = itemService.updateItem(1L, 1L, updateDto);

        assertThat(result).isNotNull();
        verify(itemRepository).save(testItem);
    }

    @Test
    void testUpdateItem_NotOwner_ThrowsException() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(anotherUser));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        assertThatThrownBy(() -> itemService.updateItem(2L, 1L, testItemDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("только владелец");
    }

    @Test
    void testSearchAvailableItems_Success() {
        when(itemRepository.searchAvailable("test")).thenReturn(java.util.List.of(testItem));
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        var result = itemService.searchAvailableItems("test");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("Test Item");
    }

    @Test
    void testSearchAvailableItems_EmptyText_ReturnsEmptyList() {
        var result = itemService.searchAvailableItems("");
        assertThat(result).isEmpty();
    }

    @Test
    void testSearchAvailableItems_NullText_ReturnsEmptyList() {
        var result = itemService.searchAvailableItems(null);
        assertThat(result).isEmpty();
    }

    @Test
    void testGetItemsByOwner_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(itemRepository.findByOwnerOrderByIdAsc(1L)).thenReturn(java.util.List.of(testItem));
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        var result = itemService.getItemsByOwner(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    // BOOKING TESTS

    @Test
    void testCreateBooking_Success() {
        User booker = new User();
        booker.setId(2L);
        booker.setName("Booker");
        booker.setEmail("booker@example.com");

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
    void testCreateBooking_ItemNotAvailable_ThrowsException() {
        testItem.setAvailable(false);
        User booker = new User();
        booker.setId(2L);

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
    void testCreateBooking_OwnerBooksOwnItem_ThrowsException() {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        assertThatThrownBy(() -> bookingService.createBooking(1L, requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не может бронировать");
    }

    @Test
    void testGetBookingById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);

        BookingDto result = bookingService.getBookingById(1L, 1L);

        assertThat(result).isNotNull();
    }

    @Test
    void testGetBookingById_NotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingById(1L, 99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найдено");
    }

    @Test
    void testApproveBooking_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        when(bookingMapper.toBookingDto(testBooking)).thenReturn(testBookingDto);

        BookingDto result = bookingService.approveBooking(1L, 1L, true);

        assertThat(result).isNotNull();
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void testApproveBooking_NotOwner_ThrowsException() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        testBooking.getItem().setOwner(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        assertThatThrownBy(() -> bookingService.approveBooking(2L, 1L, true))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Подтвердить бронирование может только владелец");
    }

    @Test
    void testApproveBooking_AlreadyApproved_ThrowsException() {
        testBooking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> bookingService.approveBooking(1L, 1L, true))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("уже имеет статус");
    }
}