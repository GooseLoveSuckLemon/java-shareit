package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void createBooking_ShouldReturnBooking() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);

        when(bookingService.createBooking(eq(1L), any(BookingRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getBookingById_ShouldReturnBooking() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setId(1L);

        when(bookingService.getBookingById(1L, 1L)).thenReturn(dto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllBookings_ShouldReturnList() throws Exception {
        when(bookingService.getBookingsByBooker(eq(1L), any(BookingState.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_ShouldReturnBooking() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setId(1L);

        when(bookingService.approveBooking(1L, 1L, true)).thenReturn(dto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_ShouldReturnList() throws Exception {
        when(bookingService.getBookingsByOwner(eq(1L), any(BookingState.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithFutureState_ShouldReturnList() throws Exception {
        when(bookingService.getBookingsByOwner(eq(1L), eq(BookingState.FUTURE)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "FUTURE"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_WithAllStates() throws Exception {
        BookingState[] states = {BookingState.ALL, BookingState.CURRENT, BookingState.FUTURE,
                BookingState.PAST, BookingState.WAITING, BookingState.REJECTED};

        for (BookingState state : states) {
            when(bookingService.getBookingsByBooker(eq(1L), eq(state)))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get("/bookings")
                            .header("X-Sharer-User-Id", 1)
                            .param("state", state.name()))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void getBookingsByOwner_WithAllStates() throws Exception {
        BookingState[] states = {BookingState.ALL, BookingState.CURRENT, BookingState.FUTURE, BookingState.PAST};

        for (BookingState state : states) {
            when(bookingService.getBookingsByOwner(eq(1L), eq(state)))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get("/bookings/owner")
                            .header("X-Sharer-User-Id", 1)
                            .param("state", state.name()))
                    .andExpect(status().isOk());
        }
    }
}