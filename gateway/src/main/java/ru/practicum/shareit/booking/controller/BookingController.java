package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

/**
 * Контроллер для управления бронированиями (Gateway).
 *
 * @author ShareIt Team
 * @version 1.0
 */
@RequestMapping("/bookings")
public interface BookingController {

    /**
     * Получение списка бронирований пользователя.
     *
     * @param userId ID пользователя
     * @param stateParam статус для фильтрации
     * @param from начальная позиция
     * @param size количество элементов
     * @return список бронирований
     */
    @GetMapping
    ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size);

    /**
     * Создание нового бронирования.
     *
     * @param userId ID пользователя
     * @param requestDto данные бронирования
     * @return созданное бронирование
     */
    @PostMapping
    ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody @Valid BookItemRequestDto requestDto);

    /**
     * Получение бронирования по ID.
     *
     * @param userId ID пользователя
     * @param bookingId ID бронирования
     * @return бронирование
     */
    @GetMapping("/{bookingId}")
    ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable Long bookingId);

    /**
     * Подтверждение или отклонение бронирования.
     *
     * @param userId ID владельца
     * @param bookingId ID бронирования
     * @param approved статус подтверждения
     * @return обновлённое бронирование
     */
    @PatchMapping("/{bookingId}")
    ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved);

    /**
     * Получение бронирований вещей пользователя.
     *
     * @param userId ID владельца
     * @param stateParam статус для фильтрации
     * @param from начальная позиция
     * @param size количество элементов
     * @return список бронирований
     */
    @GetMapping("/owner")
    ResponseEntity<Object> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size);
}