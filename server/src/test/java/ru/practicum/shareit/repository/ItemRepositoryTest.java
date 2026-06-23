package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        entityManager.persist(owner);

        item1 = new Item();
        item1.setName("Drill");
        item1.setDescription("Powerful drill");
        item1.setAvailable(true);
        item1.setOwner(owner.getId());
        entityManager.persist(item1);

        item2 = new Item();
        item2.setName("Hammer");
        item2.setDescription("Heavy hammer");
        item2.setAvailable(false);
        item2.setOwner(owner.getId());
        entityManager.persist(item2);

        entityManager.flush();
    }

    @Test
    void findByOwnerOrderByIdAsc_ShouldReturnOwnerItems() {
        List<Item> items = itemRepository.findByOwnerOrderByIdAsc(owner.getId());

        assertThat(items).hasSize(2);
        assertThat(items.get(0).getName()).isEqualTo("Drill");
        assertThat(items.get(1).getName()).isEqualTo("Hammer");
    }

    @Test
    void searchAvailable_ShouldReturnMatchingItems() {
        List<Item> items = itemRepository.searchAvailable("drill");

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo("Drill");
        assertThat(items.get(0).getAvailable()).isTrue();
    }

    @Test
    void searchAvailable_ShouldReturnEmpty_WhenNoMatch() {
        List<Item> items = itemRepository.searchAvailable("saw");

        assertThat(items).isEmpty();
    }

    @Test
    void searchAvailable_ShouldReturnEmpty_WhenItemNotAvailable() {
        List<Item> items = itemRepository.searchAvailable("hammer");

        assertThat(items).isEmpty();
    }
}