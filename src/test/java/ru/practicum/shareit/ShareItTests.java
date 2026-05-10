package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ShareItTests {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserServiceImpl userService;
    private ItemRepository itemRepository;
    private ItemMapper itemMapper;
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        userMapper = new UserMapper();
        userService = new UserServiceImpl(userRepository, userMapper);

        itemRepository = new ItemRepository();
        itemMapper = new ItemMapper();
        itemService = new ItemServiceImpl(itemRepository, userRepository, itemMapper, null);
    }

    @Test
    void testCreateUser_Success() {
        UserDto userDto = new UserDto(null, "John Doe", "john@example.com");
        UserDto result = userService.createUser(userDto);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testCreateUser_DuplicateEmail_ThrowsException() {
        UserDto userDto1 = new UserDto(null, "John Doe", "john@example.com");
        userService.createUser(userDto1);

        UserDto userDto2 = new UserDto(null, "John Doe", "john@example.com");

        assertThatThrownBy(() -> userService.createUser(userDto2))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("уже существует");
    }

    @Test
    void testGetUserById_Success() {
        UserDto userDto = new UserDto(null, "John Doe", "john@example.com");
        UserDto saved = userService.createUser(userDto);

        UserDto result = userService.getUserById(saved.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    void testGetUserById_NotFound_ThrowsException() {
        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void testUpdateUser_Success() {
        UserDto userDto = new UserDto(null, "John Doe", "john@example.com");
        UserDto saved = userService.createUser(userDto);

        UserDto updateDto = new UserDto(null, "Jane Doe", "jane@example.com");
        UserDto result = userService.updateUser(updateDto, saved.getId());

        assertThat(result.getName()).isEqualTo("Jane Doe");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void testDeleteUser_Success() {
        UserDto userDto = new UserDto(null, "John Doe", "john@example.com");
        UserDto saved = userService.createUser(userDto);

        userService.deleteUser(saved.getId());

        assertThatThrownBy(() -> userService.getUserById(saved.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testDeleteUser_NotFound_ThrowsException() {
        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void testCreateItem_Success() {
        UserDto userDto = new UserDto(null, "Owner", "owner@test.com");
        UserDto owner = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto(null, "Laptop", "Gaming laptop", true, null);
        ItemDto result = itemService.createItem(owner.getId(), itemDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");
        assertThat(result.getDescription()).isEqualTo("Gaming laptop");
        assertThat(result.getAvailable()).isTrue();
    }

    @Test
    void testCreateItem_UserNotFound_ThrowsException() {
        ItemDto itemDto = new ItemDto(null, "Laptop", "Gaming laptop", true, null);

        assertThatThrownBy(() -> itemService.createItem(99L, itemDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void testGetItemById_Success() {
        UserDto owner = userService.createUser(new UserDto(null, "Owner", "owner@test.com"));
        ItemDto saved = itemService.createItem(owner.getId(),
                new ItemDto(null, "Laptop", "Gaming laptop", true, null));

        ItemDto result = itemService.getItemById(saved.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getName()).isEqualTo("Laptop");
    }

    @Test
    void testGetItemById_NotFound_ThrowsException() {
        assertThatThrownBy(() -> itemService.getItemById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("не найдена");
    }

    @Test
    void testUpdateItem_Success() {
        UserDto owner = userService.createUser(new UserDto(null, "Owner", "owner@test.com"));
        ItemDto saved = itemService.createItem(owner.getId(),
                new ItemDto(null, "Laptop", "Gaming laptop", true, null));

        ItemDto updateDto = new ItemDto(null, "Laptop Pro", "Gaming Laptop Pro", false, null);
        ItemDto result = itemService.updateItem(owner.getId(), saved.getId(), updateDto);

        assertThat(result.getName()).isEqualTo("Laptop Pro");
        assertThat(result.getDescription()).isEqualTo("Gaming Laptop Pro");
        assertThat(result.getAvailable()).isFalse();
    }

    @Test
    void testUpdateItem_NotOwner_ThrowsException() {
        UserDto owner1 = userService.createUser(new UserDto(null, "Owner1", "owner1@test.com"));
        UserDto owner2 = userService.createUser(new UserDto(null, "Owner2", "owner2@test.com"));

        ItemDto saved = itemService.createItem(owner1.getId(),
                new ItemDto(null, "Laptop", "Gaming laptop", true, null));

        ItemDto updateDto = new ItemDto(null, "Laptop Pro", "Gaming Laptop Pro", false, null);

        assertThatThrownBy(() -> itemService.updateItem(owner2.getId(), saved.getId(), updateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("только владелец");
    }

    @Test
    void testSearchAvailableItems_Success() {
        UserDto owner = userService.createUser(new UserDto(null, "Owner", "owner@test.com"));
        itemService.createItem(owner.getId(),
                new ItemDto(null, "Gaming Laptop", "High performance laptop", true, null));
        itemService.createItem(owner.getId(),
                new ItemDto(null, "Office PC", "Desktop computer", true, null));

        var result = itemService.searchAvailableItems("laptop");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("Laptop");
    }

    @Test
    void testSearchAvailableItems_EmptyText_ReturnsEmptyList() {
        var result = itemService.searchAvailableItems("");

        assertThat(result).isEmpty();
    }

    @Test
    void testGetItemsByOwner_Success() {
        UserDto owner = userService.createUser(new UserDto(null, "Owner", "owner@test.com"));
        itemService.createItem(owner.getId(),
                new ItemDto(null, "Laptop", "Gaming laptop", true, null));
        itemService.createItem(owner.getId(),
                new ItemDto(null, "Mouse", "Wireless mouse", true, null));

        var result = itemService.getItemsByOwner(owner.getId());

        assertThat(result).hasSize(2);
    }
}