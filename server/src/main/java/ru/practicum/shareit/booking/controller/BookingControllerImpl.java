package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class BookingControllerImpl implements BookingController {

    private final BookingService bookingService;

    @Override
    public ResponseEntity<BookingDto> create(Long userId, BookingRequestDto bookingRequestDto) {
        log.info("POST /bookings - создание бронирования пользователем {}", userId);
        return ResponseEntity.ok(bookingService.createBooking(userId, bookingRequestDto));
    }

    @Override
    public ResponseEntity<BookingDto> approve(Long userId, Long bookingId, Boolean approved) {
        log.info("PATCH /bookings/{} - подтверждение бронирования пользователем {}, approved={}",
                bookingId, userId, approved);
        return ResponseEntity.ok(bookingService.approveBooking(userId, bookingId, approved));
    }

    @Override
    public ResponseEntity<BookingDto> getById(Long userId, Long bookingId) {
        log.info("GET /bookings/{} - получение бронирования пользователем {}", bookingId, userId);
        return ResponseEntity.ok(bookingService.getBookingById(userId, bookingId));
    }

    @Override
    public ResponseEntity<List<BookingDto>> getAllByBooker(Long userId, BookingState state) {
        log.info("GET /bookings - получение всех бронирований пользователя {}, state={}", userId, state);
        return ResponseEntity.ok(bookingService.getBookingsByBooker(userId, state));
    }

    @Override
    public ResponseEntity<List<BookingDto>> getAllByOwner(Long userId, BookingState state) {
        log.info("GET /bookings/owner - получение бронирований вещей владельца {}, state={}", userId, state);
        return ResponseEntity.ok(bookingService.getBookingsByOwner(userId, state));
    }
}