package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Comment.CommentMapper;
import ru.practicum.shareit.Comment.CommentRepository;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.Comment.model.Comment;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

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

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User owner;
    private User booker;
    private Item item;
    private ItemDto itemDto;
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

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(booker);
        comment.setItem(item);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("Booker");
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void createItem_ShouldSucceed() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemMapper.toItemModel(any(ItemDto.class), eq(1L), any())).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.createItem(1L, itemDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Item");
    }

    @Test
    void createItem_WithRequestId_ShouldLinkRequest() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        itemDto.setRequestId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(itemMapper.toItemModel(any(ItemDto.class), eq(1L), eq(request))).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.createItem(1L, itemDto);

        assertThat(result).isNotNull();
        verify(itemRequestRepository).findById(1L);
    }

    @Test
    void createItem_WithInvalidRequestId_ShouldThrowException() {
        itemDto.setRequestId(99L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.createItem(1L, itemDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void updateItem_ShouldUpdateName() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.updateItem(1L, 1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(item.getName()).isEqualTo("Updated Name");
    }

    @Test
    void updateItem_ShouldUpdateDescription() {
        ItemDto updateDto = new ItemDto();
        updateDto.setDescription("Updated Description");

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.updateItem(1L, 1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(item.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void updateItem_ShouldUpdateAvailable() {
        ItemDto updateDto = new ItemDto();
        updateDto.setAvailable(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.updateItem(1L, 1L, updateDto);

        assertThat(result).isNotNull();
        assertThat(item.getAvailable()).isFalse();
    }

    @Test
    void updateItem_WhenNotOwner_ShouldThrowException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> itemService.updateItem(2L, 1L, itemDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("только владелец");
    }

    @Test
    void getItemById_ShouldReturnItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.getItemById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getItemById_WhenNotFound_ShouldThrowException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найдена");
    }

    @Test
    void getItemsByOwner_ShouldReturnList() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findByOwnerOrderByIdAsc(1L)).thenReturn(Collections.singletonList(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        var result = itemService.getItemsByOwner(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void searchAvailableItems_ShouldReturnMatching() {
        when(itemRepository.searchAvailable("test")).thenReturn(Collections.singletonList(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        var result = itemService.searchAvailableItems("test");

        assertThat(result).hasSize(1);
    }

    @Test
    void searchAvailableItems_WithEmptyText_ShouldReturnEmpty() {
        var result = itemService.searchAvailableItems("");
        assertThat(result).isEmpty();
    }

    @Test
    void searchAvailableItems_WithNullText_ShouldReturnEmpty() {
        var result = itemService.searchAvailableItems(null);
        assertThat(result).isEmpty();
    }

    @Test
    void addComment_ShouldSucceed() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);

        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByBookerIdAndItemIdAndEndBeforeAndStatus(
                eq(2L), eq(1L), any(LocalDateTime.class), eq(BookingStatus.APPROVED)))
                .thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);

        CommentDto result = itemService.addComment(2L, 1L, commentDto);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo("Great item!");
    }

    @Test
    void addComment_WhenUserNeverBooked_ShouldThrowException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByBookerIdAndItemIdAndEndBeforeAndStatus(
                eq(2L), eq(1L), any(LocalDateTime.class), eq(BookingStatus.APPROVED)))
                .thenReturn(false);

        assertThatThrownBy(() -> itemService.addComment(2L, 1L, commentDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("не брал");
    }
}