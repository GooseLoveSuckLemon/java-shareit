package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.client.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemRequestControllerImpl implements ItemRequestController {

    private final RequestClient requestClient;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @Override
    public ResponseEntity<Object> create(Long userId, ItemRequestDto requestDto) {
        log.info("POST /requests - создание запроса пользователем {}", userId);
        return requestClient.createRequest(userId, requestDto);
    }

    @Override
    public ResponseEntity<Object> getOwnRequests(Long userId) {
        log.info("GET /requests - получение своих запросов пользователем {}", userId);
        return requestClient.getOwnRequests(userId);
    }

    @Override
    public ResponseEntity<Object> getAllRequests(Long userId, int from, int size) {
        log.info("GET /requests/all - получение всех запросов пользователем {}, from={}, size={}", userId, from, size);
        return requestClient.getAllRequests(userId, from, size);
    }

    @Override
    public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
        log.info("GET /requests/{} - получение запроса пользователем {}", requestId, userId);
        return requestClient.getRequestById(userId, requestId);
    }
}