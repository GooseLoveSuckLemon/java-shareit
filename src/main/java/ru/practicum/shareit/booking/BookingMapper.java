package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;

@Component
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());

        if (booking.getBooker() != null) {
            BookingDto.BookerDto bookerDto = new BookingDto.BookerDto();
            bookerDto.setId(booking.getBooker().getId());
            bookerDto.setName(booking.getBooker().getName());
            dto.setBooker(bookerDto);
        }

        if (booking.getItem() != null) {
            BookingDto.ItemForBookingDto itemDto = new BookingDto.ItemForBookingDto();
            itemDto.setId(booking.getItem().getId());
            itemDto.setName(booking.getItem().getName());
            dto.setItem(itemDto);
        }

        return dto;
    }
}