package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long itemId = 1;

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    public Optional<Item> findById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    public Item save(Item item) {
        if (item.getItemId() == null) {
            item.setItemId(itemId++);
        }
        items.put(item.getItemId(), item);
        return item;
    }

    public void deleteById(Long itemId) {
        items.remove(itemId);
    }

    public boolean existsById(Long itemId) {
        return items.containsKey(itemId);
    }

    public List<Item> finByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    public List<Item> search(String s) {
        if (s == null || s.isBlank()) return List.of();
        String lowerText = s.toLowerCase();
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerText) ||
                        item.getDescription().toLowerCase().contains(lowerText))
                .collect(Collectors.toList());
    }
}

