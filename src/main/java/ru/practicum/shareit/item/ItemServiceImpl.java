package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment.CommentRepository;
import ru.practicum.shareit.item.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + ownerId + " не найден"));

        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос c id " + itemDto.getRequestId() + " не найден"));
        }

        Item item = itemMapper.toItemModel(itemDto, ownerId, request);
        Item savedItem = itemRepository.save(item);
        log.info("Создана вещь {} пользователем {}", savedItem.getId(), ownerId);

        return itemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + ownerId + " не найден"));

        Item existing = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        if (!existing.getOwner().equals(ownerId)) {
            throw new NotFoundException("Редактировать вещь может только владелец");
        }

        if (itemDto.getName() != null) {
            existing.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existing.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existing.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(existing);
        log.info("Обновлена вещь {}", itemId);

        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemWithBookingsDto getItemWithBookingsAndComments(Long userId, Long itemId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + userId + " не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        LocalDateTime now = LocalDateTime.now();

        BookingShortDto lastBooking = null;
        BookingShortDto nextBooking = null;

        if (item.getOwner().equals(userId)) {
            lastBooking = bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(itemId, now, BookingStatus.APPROVED)
                    .map(this::mapToBookingShortDto)
                    .orElse(null);

            nextBooking = bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId, now, BookingStatus.APPROVED)
                    .map(this::mapToBookingShortDto)
                    .orElse(null);
        }

        List<CommentDto> comments = commentRepository.findByItemIdOrderByCreatedDesc(itemId)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());

        return itemMapper.toItemWithBookingsDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + ownerId + " не найден"));

        return itemRepository.findByOwnerOrderByIdAsc(ownerId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemWithBookingsDto> getItemsWithBookingsAndCommentsByOwner(Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + ownerId + " не найден"));

        LocalDateTime now = LocalDateTime.now();

        return itemRepository.findByOwnerOrderByIdAsc(ownerId).stream()
                .map(item -> {
                    BookingShortDto lastBooking = bookingRepository
                            .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), now, BookingStatus.APPROVED)
                            .map(this::mapToBookingShortDto)
                            .orElse(null);

                    BookingShortDto nextBooking = bookingRepository
                            .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), now, BookingStatus.APPROVED)
                            .map(this::mapToBookingShortDto)
                            .orElse(null);

                    List<CommentDto> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId())
                            .stream()
                            .map(commentMapper::toCommentDto)
                            .collect(Collectors.toList());

                    return itemMapper.toItemWithBookingsDto(item, lastBooking, nextBooking, comments);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchAvailableItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchAvailable(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + userId + " не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        LocalDateTime now = LocalDateTime.now();

        boolean hasBookedAndFinished = bookingRepository
                .existsByBookerIdAndItemIdAndEndBeforeAndStatus(userId, itemId, now, BookingStatus.APPROVED);

        if (!hasBookedAndFinished) {
            throw new BadRequestException("Пользователь не брал эту вещь в аренду или аренда ещё не завершена");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        log.info("Добавлен комментарий к вещи {} пользователем {}", itemId, userId);

        return commentMapper.toCommentDto(savedComment);
    }

    private BookingShortDto mapToBookingShortDto(Booking booking) {
        BookingShortDto dto = new BookingShortDto();
        dto.setId(booking.getId());
        dto.setBookerId(booking.getBooker().getId());
        dto.setBookerName(booking.getBooker().getName());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }
}