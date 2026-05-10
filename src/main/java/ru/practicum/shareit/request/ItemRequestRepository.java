package ru.practicum.shareit.request;

import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class ItemRequestRepository {
    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private long currentId = 1;

    public Optional<ItemRequest> findById(Long id) {
        return Optional.ofNullable(requests.get(id));
    }

    public ItemRequest save(ItemRequest request) {
        if (request.getId() == null) {
            request.setId(currentId++);
        }
        requests.put(request.getId(), request);
        return request;
    }

    public List<ItemRequest> findAll() {
        return new ArrayList<>(requests.values());
    }

    public void deleteById(Long id) {
        requests.remove(id);
    }
}