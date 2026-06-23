package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingControllerImpl;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BookingClient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingControllerImpl.class)
class BookingControllerImplParamTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Test
    void getBookings_WithFromAndSizeParams() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "5")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_WithLargeFromAndSize() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "100")
                        .param("size", "50"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_WithFromAndSizeParams() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "5")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_WithAllStates() throws Exception {
        for (BookingState state : BookingState.values()) {
            mockMvc.perform(get("/bookings")
                            .header("X-Sharer-User-Id", 1)
                            .param("state", state.name())
                            .param("from", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk());
        }
    }
}