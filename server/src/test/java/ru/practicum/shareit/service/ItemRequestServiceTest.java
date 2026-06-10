package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    private User requestor;
    private User otherUser;
    private ItemRequest testRequest;
    private ItemRequest otherRequest;
    private ItemRequestDto testRequestDto;
    private ItemRequestDto otherRequestDto;
    private Item itemWithRequest;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        requestor = new User();
        requestor.setId(1L);
        requestor.setName("Requestor");
        requestor.setEmail("requestor@example.com");

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setName("Other");
        otherUser.setEmail("other@example.com");

        testRequest = new ItemRequest();
        testRequest.setId(1L);
        testRequest.setDescription("Need a drill");
        testRequest.setRequestor(requestor);
        testRequest.setCreated(LocalDateTime.now());

        otherRequest = new ItemRequest();
        otherRequest.setId(2L);
        otherRequest.setDescription("Need a hammer");
        otherRequest.setRequestor(otherUser);
        otherRequest.setCreated(LocalDateTime.now());

        testRequestDto = new ItemRequestDto();
        testRequestDto.setId(1L);
        testRequestDto.setDescription("Need a drill");
        testRequestDto.setRequestorId(1L);
        testRequestDto.setCreated(LocalDateTime.now());
        testRequestDto.setItems(Collections.emptyList());

        otherRequestDto = new ItemRequestDto();
        otherRequestDto.setId(2L);
        otherRequestDto.setDescription("Need a hammer");
        otherRequestDto.setRequestorId(2L);
        otherRequestDto.setCreated(LocalDateTime.now());
        otherRequestDto.setItems(Collections.emptyList());

        itemWithRequest = new Item();
        itemWithRequest.setId(1L);
        itemWithRequest.setName("Drill");
        itemWithRequest.setDescription("Powerful drill");
        itemWithRequest.setAvailable(true);
        itemWithRequest.setOwner(2L);
        itemWithRequest.setRequest(testRequest);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
    }

    @Test
    void createRequest_ShouldSucceed_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(testRequest);
        when(itemRequestMapper.toDto(testRequest)).thenReturn(testRequestDto);

        ItemRequestDto result = requestService.createRequest(1L, testRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Need a drill");
        assertThat(result.getId()).isEqualTo(1L);
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void createRequest_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestService.createRequest(99L, testRequestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getRequestById_ShouldReturnRequest_WhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(testRequest));
        when(itemRequestMapper.toDto(testRequest)).thenReturn(testRequestDto);
        when(itemRepository.findAll()).thenReturn(List.of(itemWithRequest));
        when(itemMapper.toItemDto(itemWithRequest)).thenReturn(itemDto);

        ItemRequestDto result = requestService.getRequestById(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Need a drill");
        assertThat(result.getItems()).hasSize(1);
        verify(itemRepository).findAll();
    }

    @Test
    void getRequestById_ShouldThrowNotFoundException_WhenRequestNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestService.getRequestById(1L, 99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getRequestById_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestService.getRequestById(99L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getOwnRequests_ShouldReturnOnlyOwnRequests_WhenUserExists() {
        List<ItemRequest> allRequests = List.of(testRequest, otherRequest);

        when(userRepository.findById(1L)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findAll()).thenReturn(allRequests);
        when(itemRequestMapper.toDto(testRequest)).thenReturn(testRequestDto);
        when(itemRepository.findAll()).thenReturn(List.of(itemWithRequest));
        when(itemMapper.toItemDto(itemWithRequest)).thenReturn(itemDto);

        List<ItemRequestDto> result = requestService.getOwnRequests(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Need a drill");
        assertThat(result.get(0).getRequestorId()).isEqualTo(1L);
        verify(itemRequestRepository).findAll();
    }

    @Test
    void getOwnRequests_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestService.getOwnRequests(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getAllRequests_ShouldReturnOtherUsersRequests_WhenUserExists() {
        List<ItemRequest> allRequests = List.of(testRequest, otherRequest);

        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        when(itemRequestRepository.findAll()).thenReturn(allRequests);
        when(itemRequestMapper.toDto(testRequest)).thenReturn(testRequestDto);
        when(itemRepository.findAll()).thenReturn(List.of(itemWithRequest));
        when(itemMapper.toItemDto(itemWithRequest)).thenReturn(itemDto);

        List<ItemRequestDto> result = requestService.getAllRequests(2L, 0, 10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Need a drill");
        assertThat(result.get(0).getRequestorId()).isEqualTo(1L);
        verify(itemRequestRepository).findAll();
    }

    @Test
    void getAllRequests_ShouldReturnEmptyList_WhenNoOtherRequests() {
        List<ItemRequest> allRequests = List.of(testRequest);

        when(userRepository.findById(1L)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findAll()).thenReturn(allRequests);

        List<ItemRequestDto> result = requestService.getAllRequests(1L, 0, 10);

        assertThat(result).isEmpty();
        verify(itemRequestRepository).findAll();
        verify(itemRepository, never()).findAll();
        verify(itemRequestMapper, never()).toDto(any());
    }

    @Test
    void getAllRequests_ShouldThrowNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestService.getAllRequests(99L, 0, 10))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }
}