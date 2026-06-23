package ru.practicum.shareit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRequestRepository requestRepository;

    private User requestor;
    private User otherUser;
    private ItemRequest request1;
    private ItemRequest request2;

    @BeforeEach
    void setUp() {
        requestor = new User();
        requestor.setName("Requestor");
        requestor.setEmail("requestor@example.com");
        entityManager.persist(requestor);

        otherUser = new User();
        otherUser.setName("Other");
        otherUser.setEmail("other@example.com");
        entityManager.persist(otherUser);

        request1 = new ItemRequest();
        request1.setDescription("Need a drill");
        request1.setRequestor(requestor);
        request1.setCreated(LocalDateTime.now().minusDays(2));
        entityManager.persist(request1);

        request2 = new ItemRequest();
        request2.setDescription("Need a hammer");
        request2.setRequestor(requestor);
        request2.setCreated(LocalDateTime.now().minusDays(1));
        entityManager.persist(request2);

        entityManager.flush();
    }

    @Test
    void findAllByRequestorIdOrderByCreatedDesc_ShouldReturnRequestsOrdered() {
        List<ItemRequest> requests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(requestor.getId());

        assertThat(requests).hasSize(2);
        assertThat(requests.get(0).getDescription()).isEqualTo("Need a hammer");
        assertThat(requests.get(1).getDescription()).isEqualTo("Need a drill");
    }

    @Test
    void findAllByRequestorIdNot_ShouldReturnOtherUsersRequests() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ItemRequest> requests = requestRepository.findAllByRequestorIdNot(otherUser.getId(), pageRequest);

        assertThat(requests.getContent()).hasSize(2);
    }
}