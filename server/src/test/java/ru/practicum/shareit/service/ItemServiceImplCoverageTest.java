package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Comment.repository.CommentRepository;
import ru.practicum.shareit.Comment.model.Comment;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.Comment.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplCoverageTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User owner;
    private User booker;
    private Item item;
    private ItemDto itemDto;
    private Booking lastBooking;
    private Booking nextBooking;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");

        booker = new User();
        booker.setId(2L);
        booker.setName("Booker");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(1L);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        lastBooking = new Booking();
        lastBooking.setId(1L);
        lastBooking.setStart(LocalDateTime.now().minusDays(2));
        lastBooking.setEnd(LocalDateTime.now().minusDays(1));
        lastBooking.setStatus(BookingStatus.APPROVED);
        lastBooking.setBooker(booker);
        lastBooking.setItem(item);

        nextBooking = new Booking();
        nextBooking.setId(2L);
        nextBooking.setStart(LocalDateTime.now().plusDays(1));
        nextBooking.setEnd(LocalDateTime.now().plusDays(2));
        nextBooking.setStatus(BookingStatus.APPROVED);
        nextBooking.setBooker(booker);
        nextBooking.setItem(item);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(owner);
        comment.setItem(item);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("Owner");
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void createItem_WithRequestId_ShouldLinkRequest() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(itemMapper.toItemModel(any(ItemDto.class), eq(1L), eq(request))).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        itemDto.setRequestId(1L);
        ItemDto result = itemService.createItem(1L, itemDto);

        assertThat(result).isNotNull();
        verify(itemRequestRepository).findById(1L);
    }

    @Test
    void createItem_WithInvalidRequestId_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(99L)).thenReturn(Optional.empty());

        itemDto.setRequestId(99L);

        assertThatThrownBy(() -> itemService.createItem(1L, itemDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void updateItem_WithNullFields_ShouldNotUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto updateDto = new ItemDto();
        ItemDto result = itemService.updateItem(1L, 1L, updateDto);

        assertThat(result).isNotNull();
        verify(itemRepository).save(item);
    }

    @Test
    void getItemById_WhenItemNotFound_ShouldThrowException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateItem_WhenItemNotFound_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.updateItem(1L, 99L, itemDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateItem_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.updateItem(99L, 1L, itemDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addComment_WhenItemNotFound_ShouldThrowException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.addComment(2L, 99L, commentDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addComment_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.addComment(99L, 1L, commentDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getItemsByOwner_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemsByOwner(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getItemsWithBookingsAndCommentsByOwner_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemsWithBookingsAndCommentsByOwner(99L))
                .isInstanceOf(NotFoundException.class);
    }
}