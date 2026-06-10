package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.Comment.CommentRepository;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    @InjectMocks
    private ItemServiceImpl itemService;

    private User owner;
    private Item testItem;
    private ItemDto testItemDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");
        owner.setEmail("owner@example.com");

        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Test Item");
        testItem.setDescription("Test Description");
        testItem.setAvailable(true);
        testItem.setOwner(1L);

        testItemDto = new ItemDto();
        testItemDto.setId(1L);
        testItemDto.setName("Test Item");
        testItemDto.setDescription("Test Description");
        testItemDto.setAvailable(true);
    }

    @Test
    void createItem_ShouldSucceed_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemMapper.toItemModel(any(ItemDto.class), eq(1L), any())).thenReturn(testItem);
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        ItemDto result = itemService.createItem(1L, testItemDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Item");
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void createItem_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.createItem(99L, testItemDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getItemById_ShouldReturnItem_WhenExists() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        ItemDto result = itemService.getItemById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void updateItem_ShouldSucceed_WhenUserIsOwner() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Item");

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        ItemDto result = itemService.updateItem(1L, 1L, updateDto);

        assertThat(result).isNotNull();
        verify(itemRepository).save(testItem);
    }

    @Test
    void updateItem_ShouldThrowException_WhenUserIsNotOwner() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        assertThatThrownBy(() -> itemService.updateItem(2L, 1L, testItemDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("только владелец");
    }

    @Test
    void searchAvailableItems_ShouldReturnEmpty_WhenTextIsBlank() {
        var result = itemService.searchAvailableItems("");
        assertThat(result).isEmpty();
    }

    @Test
    void getItemById_WhenItemExists_ShouldReturnItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        ItemDto result = itemService.getItemById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getItemById_WhenItemNotFound_ShouldThrowException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найдена");
    }

    @Test
    void getItemsByOwner_WhenOwnerExists_ShouldReturnItems() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findByOwnerOrderByIdAsc(1L)).thenReturn(Collections.singletonList(testItem));
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        var result = itemService.getItemsByOwner(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getItemsByOwner_WhenOwnerNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemsByOwner(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void searchAvailableItems_WithValidText_ShouldReturnItems() {
        when(itemRepository.searchAvailable("test")).thenReturn(Collections.singletonList(testItem));
        when(itemMapper.toItemDto(testItem)).thenReturn(testItemDto);

        var result = itemService.searchAvailableItems("test");

        assertThat(result).hasSize(1);
    }

    @Test
    void searchAvailableItems_WithEmptyText_ShouldReturnEmptyList() {
        var result = itemService.searchAvailableItems("");
        assertThat(result).isEmpty();
    }

    @Test
    void searchAvailableItems_WithNullText_ShouldReturnEmptyList() {
        var result = itemService.searchAvailableItems(null);
        assertThat(result).isEmpty();
    }
}