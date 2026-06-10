package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto create(@RequestHeader(SHARER_USER_ID) Long userId,
                                 @RequestBody ItemRequestDto requestDto) {
        log.info("REQUEST to server: POST /requests by user {}", userId);
        return requestService.createRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getOwn(@RequestHeader(SHARER_USER_ID) Long userId) {
        log.info("REQUEST to server: GET /requests for user {}", userId);
        return requestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(SHARER_USER_ID) Long userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("REQUEST to server: GET /requests/all for user {}, from={}, size={}", userId, from, size);
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(SHARER_USER_ID) Long userId,
                                  @PathVariable Long requestId) {
        log.info("REQUEST to server: GET /requests/{} for user {}", requestId, userId);
        return requestService.getRequestById(userId, requestId);
    }
}