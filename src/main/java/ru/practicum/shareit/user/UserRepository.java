package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User save(User user) {
        if (user.getId() == null) {
            if (users.values().stream()
                    .anyMatch(existing -> existing.getEmail().equals(user.getEmail()))) {
                throw new RuntimeException("Пользователь с email " + user.getEmail() + " уже существует");
            }
            user.setId(currentId++);
        } else {
            boolean emailExists = users.values().stream()
                    .filter(u -> !u.getId().equals(user.getId()))
                    .anyMatch(existing -> existing.getEmail().equals(user.getEmail()));
            if (emailExists) {
                throw new RuntimeException("Пользователь с email " + user.getEmail() + " уже существует");
            }
        }
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public void deleteById(Long id) {
        users.remove(id);
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}