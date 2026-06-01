package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemForBookingDto;

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
            BookerDto bookerDto = new BookerDto();
            bookerDto.setId(booking.getBooker().getId());
            bookerDto.setName(booking.getBooker().getName());
            dto.setBooker(bookerDto);
        }

        if (booking.getItem() != null) {
            ItemForBookingDto itemDto = new ItemForBookingDto();
            itemDto.setId(booking.getItem().getId());
            itemDto.setName(booking.getItem().getName());
            dto.setItem(itemDto);
        }

        return dto;
    }
}