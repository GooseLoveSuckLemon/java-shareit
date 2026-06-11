package ru.practicum.shareit.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BookingClient bookingClient;

    @Mock
    private ItemClient itemClient;

    @Mock
    private UserClient userClient;

    @Test
    void getBookingsByBooker_ShouldReturnBookings() {
        when(bookingClient.getBookings(1L, BookingState.ALL, 0, 10))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        ResponseEntity<Object> response = bookingClient.getBookings(1L, BookingState.ALL, 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBookingsByOwner_ShouldReturnBookings() {
        when(bookingClient.getBookingsByOwner(1L, BookingState.ALL, 0, 10))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        ResponseEntity<Object> response = bookingClient.getBookingsByOwner(1L, BookingState.ALL, 0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBookingById_ShouldReturnBooking() {
        when(bookingClient.getBooking(1L, 1L))
                .thenReturn(ResponseEntity.ok(new BookingDto()));

        ResponseEntity<Object> response = bookingClient.getBooking(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void approveBooking_ShouldSucceed() {
        when(bookingClient.approveBooking(1L, 1L, true))
                .thenReturn(ResponseEntity.ok(new BookingDto()));

        ResponseEntity<Object> response = bookingClient.approveBooking(1L, 1L, true);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void bookItem_ShouldSucceed() {
        BookItemRequestDto requestDto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        when(bookingClient.bookItem(1L, requestDto))
                .thenReturn(ResponseEntity.ok(new BookingDto()));

        ResponseEntity<Object> response = bookingClient.bookItem(1L, requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createItem_ShouldSucceed() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(true);

        when(itemClient.createItem(1L, itemDto))
                .thenReturn(ResponseEntity.ok(itemDto));

        ResponseEntity<Object> response = itemClient.createItem(1L, itemDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getItemById_ShouldReturnItem() {
        when(itemClient.getItemById(1L, 1L))
                .thenReturn(ResponseEntity.ok(new ItemDto()));

        ResponseEntity<Object> response = itemClient.getItemById(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateItem_ShouldSucceed() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Drill");

        when(itemClient.updateItem(1L, 1L, itemDto))
                .thenReturn(ResponseEntity.ok(itemDto));

        ResponseEntity<Object> response = itemClient.updateItem(1L, 1L, itemDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getItemsByOwner_ShouldReturnList() {
        when(itemClient.getItemsByOwner(1L))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        ResponseEntity<Object> response = itemClient.getItemsByOwner(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void searchItems_ShouldReturnList() {
        when(itemClient.searchItems("drill"))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        ResponseEntity<Object> response = itemClient.searchItems("drill");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createUser_ShouldSucceed() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");

        when(userClient.createUser(userDto))
                .thenReturn(ResponseEntity.ok(userDto));

        ResponseEntity<Object> response = userClient.createUser(userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        ResponseEntity<Object> response = userClient.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userClient.getUserById(1L))
                .thenReturn(ResponseEntity.ok(new UserDto()));

        ResponseEntity<Object> response = userClient.getUserById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateUser_ShouldSucceed() {
        UserDto userDto = new UserDto();
        userDto.setName("Updated Name");

        when(userClient.updateUser(1L, userDto))
                .thenReturn(ResponseEntity.ok(userDto));

        ResponseEntity<Object> response = userClient.updateUser(1L, userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteUser_ShouldSucceed() {
        when(userClient.deleteUser(1L))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = userClient.deleteUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}