package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequestDto itemRequestDto) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + userId));

        ItemRequest request = new ItemRequest();
        request.setDescription(itemRequestDto.getDescription());
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());

        return itemRequestMapper.toDto(itemRequestRepository.save(request));
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + userId));

        return itemRequestRepository.findAll().stream()
                .filter(request -> request.getRequestor().getId().equals(userId))
                .map(this::enrichWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, int from, int size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + userId));

        return itemRequestRepository.findAll().stream()
                .filter(request -> !request.getRequestor().getId().equals(userId))
                .skip(from)
                .limit(size)
                .map(this::enrichWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + userId));

        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден с id: " + requestId));

        return enrichWithItems(request);
    }

    private ItemRequestDto enrichWithItems(ItemRequest request) {
        ItemRequestDto dto = itemRequestMapper.toDto(request);

        List<ItemDto> items = itemRepository.findAll().stream()
                .filter(item -> item.getRequest() != null && item.getRequest().getId().equals(request.getId()))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }
}