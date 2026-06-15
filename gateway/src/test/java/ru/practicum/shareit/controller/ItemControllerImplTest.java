package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.item.controller.ItemControllerImpl;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemControllerImpl.class)
class ItemControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    @Test
    void createItem_WithValidData_ShouldReturnOk() throws Exception {
        ItemDto dto = new ItemDto(null, "Drill", "Powerful drill", true, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_ShouldReturnOk() throws Exception {
        ItemDto dto = new ItemDto(null, "Updated Drill", null, null, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getItemsByOwner_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "drill"))
                .andExpect(status().isOk());
    }

    @Test
    void addComment_WithValidData_ShouldReturnOk() throws Exception {
        CommentDto dto = new CommentDto(null, "Great item!", null, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_WithEmptyBody_ShouldReturnOk() throws Exception {
        String json = "{}";

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById_WithInvalidId_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/items/999")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems_WithEmptyText_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", ""))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems_WithBlankText_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "   "))
                .andExpect(status().isOk());
    }

    @Test
    void createItem_WithRequestId_ShouldReturnOk() throws Exception {
        ItemDto dto = new ItemDto(null, "Drill", "Powerful drill", true, 1L);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void createItem_WithoutRequestId_ShouldReturnOk() throws Exception {
        ItemDto dto = new ItemDto(null, "Hammer", "Heavy hammer", true, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_WithAllFields_ShouldReturnOk() throws Exception {
        ItemDto dto = new ItemDto(null, "Updated Drill", "Updated description", false, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_WithPartialFields_ShouldReturnOk() throws Exception {
        ItemDto dto = new ItemDto(null, null, "Only description updated", null, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void addComment_WithValidText_ShouldReturnOk() throws Exception {
        CommentDto dto = new CommentDto(null, "Great item!", null, null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}