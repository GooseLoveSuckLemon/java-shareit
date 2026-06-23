package ru.practicum.shareit.booking.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

/**
 * Контроллер для управления бронированиями.
 * Предоставляет endpoints для создания, подтверждения, получения и фильтрации бронирований.
 */
@RequestMapping("/bookings")
public interface BookingController {

    /**
     * Создание нового бронирования.
     *
     * @param userId ID пользователя, который бронирует вещь (из заголовка X-Sharer-User-Id)
     * @param bookingRequestDto данные бронирования (ID вещи, даты начала и конца)
     * @return созданное бронирование с присвоенным ID и статусом WAITING
     * @throws ru.practicum.shareit.exception.NotFoundException если пользователь или вещь не найдены
     * @throws ru.practicum.shareit.exception.BadRequestException если даты некорректны или вещь недоступна
     */
    @PostMapping
    ResponseEntity<BookingDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestBody BookingRequestDto bookingRequestDto);

    /**
     * Подтверждение или отклонение бронирования владельцем вещи.
     *
     * @param userId ID владельца вещи
     * @param bookingId ID бронирования
     * @param approved true - подтвердить, false - отклонить
     * @return обновлённое бронирование с новым статусом
     * @throws ru.practicum.shareit.exception.ForbiddenException если пользователь не владелец вещи
     * @throws ru.practicum.shareit.exception.BadRequestException если бронирование уже подтверждено/отклонено
     */
    @PatchMapping("/{bookingId}")
    ResponseEntity<BookingDto> approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId,
                                       @RequestParam Boolean approved);

    /**
     * Получение бронирования по ID.
     * Доступно для автора бронирования или владельца вещи.
     *
     * @param userId ID текущего пользователя
     * @param bookingId ID бронирования
     * @return бронирование с указанным ID
     */
    @GetMapping("/{bookingId}")
    ResponseEntity<BookingDto> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId);

    /**
     * Получение всех бронирований пользователя (как booker'а).
     *
     * @param userId ID пользователя
     * @param state статус бронирований для фильтрации (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @return список бронирований, соответствующих фильтру
     */
    @GetMapping
    ResponseEntity<List<BookingDto>> getAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "ALL") BookingState state);

    /**
     * Получение всех бронирований вещей, принадлежащих пользователю (как owner'у).
     *
     * @param userId ID владельца
     * @param state статус бронирований для фильтрации
     * @return список бронирований вещей владельца
     */
    @GetMapping("/owner")
    ResponseEntity<List<BookingDto>> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state);
}
