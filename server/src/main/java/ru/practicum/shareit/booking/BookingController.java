package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto create(@RequestHeader(SHARER_USER_ID) Long userId,
                             @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("POST /bookings - создание бронирования пользователем {}", userId);
        return bookingService.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(SHARER_USER_ID) Long userId,
                              @PathVariable Long bookingId,
                              @RequestParam Boolean approved) {
        log.info("PATCH /bookings/{} - подтверждение бронирования пользователем {}, approved={}",
                bookingId, userId, approved);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(SHARER_USER_ID) Long userId,
                              @PathVariable Long bookingId) {
        log.info("GET /bookings/{} - получение бронирования пользователем {}", bookingId, userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestHeader(SHARER_USER_ID) Long userId,
                                           @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings - получение всех бронирований пользователя {}, state={}", userId, state);
        return bookingService.getBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(SHARER_USER_ID) Long userId,
                                          @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings/owner - получение бронирований вещей владельца {}, state={}", userId, state);
        return bookingService.getBookingsByOwner(userId, state);
    }
}