package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemRequestControllerImpl implements ItemRequestController {

    private final ItemRequestService requestService;

    @Override
    public ResponseEntity<ItemRequestDto> create(Long userId, ItemRequestDto requestDto) {
        log.info("POST /requests - создание запроса пользователем {}", userId);
        return ResponseEntity.ok(requestService.createRequest(userId, requestDto));
    }

    @Override
    public ResponseEntity<List<ItemRequestDto>> getOwnRequests(Long userId) {
        log.info("GET /requests - получение своих запросов пользователем {}", userId);
        return ResponseEntity.ok(requestService.getOwnRequests(userId));
    }

    @Override
    public ResponseEntity<List<ItemRequestDto>> getAllRequests(Long userId, int from, int size) {
        log.info("GET /requests/all - получение всех запросов пользователем {}, from={}, size={}", userId, from, size);
        return ResponseEntity.ok(requestService.getAllRequests(userId, from, size));
    }

    @Override
    public ResponseEntity<ItemRequestDto> getRequestById(Long userId, Long requestId) {
        log.info("GET /requests/{} - получение запроса пользователем {}", requestId, userId);
        return ResponseEntity.ok(requestService.getRequestById(userId, requestId));
    }
}