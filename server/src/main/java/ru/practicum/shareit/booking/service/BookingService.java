package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

/**
 * Сервисный слой для управления бизнес-логикой бронирований.
 */
public interface BookingService {

    /**
     * Создаёт новое бронирование.
     *
     * @param userId ID пользователя, создающего бронирование
     * @param requestDto данные бронирования
     * @return DTO созданного бронирования
     * @throws ru.practicum.shareit.exception.NotFoundException если пользователь или вещь не найдены
     * @throws ru.practicum.shareit.exception.BadRequestException если даты некорректны, вещь недоступна или владелец пытается забронировать свою вещь
     */
    BookingDto createBooking(Long userId, BookingRequestDto requestDto);

    /**
     * Подтверждает или отклоняет бронирование.
     *
     * @param userId ID владельца вещи
     * @param bookingId ID бронирования
     * @param approved флаг подтверждения
     * @return обновлённое бронирование
     */
    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved);

    /**
     * Возвращает бронирование по ID с проверкой прав доступа.
     *
     * @param userId ID текущего пользователя
     * @param bookingId ID бронирования
     * @return DTO бронирования
     */
    BookingDto getBookingById(Long userId, Long bookingId);

    /**
     * Возвращает список бронирований пользователя как booker'а с фильтрацией по статусу.
     *
     * @param userId ID пользователя
     * @param state статус для фильтрации
     * @return список DTO бронирований
     */
    List<BookingDto> getBookingsByBooker(Long userId, BookingState state);

    /**
     * Возвращает список бронирований вещей пользователя как owner'а с фильтрацией по статусу.
     *
     * @param userId ID владельца
     * @param state статус для фильтрации
     * @return список DTO бронирований
     */
    List<BookingDto> getBookingsByOwner(Long userId, BookingState state);
}