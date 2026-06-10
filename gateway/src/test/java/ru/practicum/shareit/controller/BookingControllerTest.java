package ru.practicum.shareit.controller;

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
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    @Test
    void createBooking_WithValidInput_ShouldReturnOk() throws Exception {
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
    void createBooking_WithPastStartDate_ShouldReturnBadRequest() throws Exception {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBooking_WithPastEndDate_ShouldReturnBadRequest() throws Exception {
        BookItemRequestDto dto = new BookItemRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(1)
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingById_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_ShouldReturnOk() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void createBooking_WithValidData_ShouldReturnOk() throws Exception {
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
    void getAllBookings_WithFutureState_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "FUTURE"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithPastState_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "PAST"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithCurrentState_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "CURRENT"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithWaitingState_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "WAITING"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithRejectedState_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "REJECTED"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_WithAllStates() throws Exception {
        for (BookingState state : BookingState.values()) {
            mockMvc.perform(get("/bookings")
                            .header("X-Sharer-User-Id", 1)
                            .param("state", state.name()))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void getBookingsByOwner_WithAllStates() throws Exception {
        for (BookingState state : BookingState.values()) {
            mockMvc.perform(get("/bookings/owner")
                            .header("X-Sharer-User-Id", 1)
                            .param("state", state.name()))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void getBookings_WithStateAll_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_WithStateCurrent_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "CURRENT"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_WithStateFuture_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "FUTURE"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_WithStatePast_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "PAST"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_WithStateWaiting_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "WAITING"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_WithStateRejected_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "REJECTED"))
                .andExpect(status().isOk());
    }

}