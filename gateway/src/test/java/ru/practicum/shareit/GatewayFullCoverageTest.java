package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookingController.class, ItemController.class, ItemRequestController.class, UserController.class})
class GatewayFullCoverageTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    @MockBean
    private ItemClient itemClient;

    @MockBean
    private RequestClient requestClient;

    @MockBean
    private UserClient userClient;

    // BookingController tests
    @Test
    void bookingController_GetBookings_WithAllStates() throws Exception {
        String[] states = {"ALL", "CURRENT", "FUTURE", "PAST", "WAITING", "REJECTED"};
        for (String state : states) {
            mockMvc.perform(get("/bookings")
                            .header("X-Sharer-User-Id", 1)
                            .param("state", state))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void bookingController_GetBookingsByOwner_WithAllStates() throws Exception {
        String[] states = {"ALL", "CURRENT", "FUTURE", "PAST"};
        for (String state : states) {
            mockMvc.perform(get("/bookings/owner")
                            .header("X-Sharer-User-Id", 1)
                            .param("state", state))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void bookingController_CreateBooking_WithValidData() throws Exception {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void bookingController_GetBookingById() throws Exception {
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void bookingController_ApproveBooking() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    // ItemController tests
    @Test
    void itemController_CreateItem() throws Exception {
        ItemDto dto = new ItemDto(null, "Drill", "Powerful drill", true, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void itemController_UpdateItem() throws Exception {
        ItemDto dto = new ItemDto(null, "Updated Drill", null, null, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void itemController_GetItemById() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void itemController_GetItemsByOwner() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void itemController_SearchItems() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "drill"))
                .andExpect(status().isOk());
    }

    // ItemRequestController tests
    @Test
    void itemRequestController_CreateRequest() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need a drill");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void itemRequestController_GetOwnRequests() throws Exception {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void itemRequestController_GetAllRequests() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void itemRequestController_GetRequestById() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    // UserController tests
    @Test
    void userController_CreateUser() throws Exception {
        UserDto dto = new UserDto(null, "John Doe", "john@example.com");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void userController_GetUserById() throws Exception {
        mockMvc.perform(get("/users/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void userController_GetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void userController_UpdateUser() throws Exception {
        UserDto dto = new UserDto(null, "Updated Name", null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void userController_DeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}