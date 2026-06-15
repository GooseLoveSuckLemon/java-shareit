package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.client.BookingClient;
import ru.practicum.shareit.booking.controller.BookingControllerImpl;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingControllerImpl.class)
class BookingControllerImplTest {

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

    @Test
    void getAllBookings_WithAllStateParam_ShouldWork() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithRejectedState_ShouldWork() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "REJECTED")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithDefaultParams_ShouldWork() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void createBooking_ShouldReturnBooking() throws Exception {
        BookItemRequestDto request = new BookItemRequestDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        when(bookingClient.bookItem(eq(1L), any(BookItemRequestDto.class))).thenReturn(ResponseEntity.ok(new BookingDto()));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingById_ShouldReturnBooking() throws Exception {
        when(bookingClient.getBooking(eq(1L), eq(1L))).thenReturn(ResponseEntity.ok(new BookingDto()));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithAllState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookings(eq(1L), eq(BookingState.ALL), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithCurrentState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookings(eq(1L), eq(BookingState.CURRENT), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "CURRENT"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithFutureState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookings(eq(1L), eq(BookingState.FUTURE), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "FUTURE"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithPastState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookings(eq(1L), eq(BookingState.PAST), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "PAST"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithWaitingState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookings(eq(1L), eq(BookingState.WAITING), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "WAITING"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithRejectedState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookings(eq(1L), eq(BookingState.REJECTED), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "REJECTED"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithDefaultParams_ShouldReturnList() throws Exception {
        when(bookingClient.getBookings(eq(1L), eq(BookingState.ALL), eq(0), eq(20)))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithAllState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookingsByOwner(eq(1L), eq(BookingState.ALL), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithCurrentState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookingsByOwner(eq(1L), eq(BookingState.CURRENT), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "CURRENT"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithFutureState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookingsByOwner(eq(1L), eq(BookingState.FUTURE), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "FUTURE"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithPastState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookingsByOwner(eq(1L), eq(BookingState.PAST), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "PAST"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithWaitingState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookingsByOwner(eq(1L), eq(BookingState.WAITING), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "WAITING"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithRejectedState_ShouldReturnList() throws Exception {
        when(bookingClient.getBookingsByOwner(eq(1L), eq(BookingState.REJECTED), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "REJECTED"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithDefaultParams_ShouldReturnList() throws Exception {
        when(bookingClient.getBookingsByOwner(eq(1L), eq(BookingState.ALL), eq(0), eq(20)))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_ShouldReturnBooking() throws Exception {
        when(bookingClient.approveBooking(eq(1L), eq(1L), eq(true))).thenReturn(ResponseEntity.ok(new BookingDto()));

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void rejectBooking_ShouldReturnBooking() throws Exception {
        when(bookingClient.approveBooking(eq(1L), eq(1L), eq(false))).thenReturn(ResponseEntity.ok(new BookingDto()));

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "false"))
                .andExpect(status().isOk());
    }

}