package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Comment.dto.CommentDto;
import ru.practicum.shareit.item.controller.ItemControllerImpl;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;


import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemControllerImpl.class)
class ItemControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void createItem_ShouldReturnItem() throws Exception {
        ItemDto inputDto = new ItemDto(null, "Drill", "Powerful drill", true, null);
        ItemDto outputDto = new ItemDto(1L, "Drill", "Powerful drill", true, null);

        when(itemService.createItem(eq(1L), any(ItemDto.class))).thenReturn(outputDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Drill"));
    }

    @Test
    void getItemById_ShouldReturnItem() throws Exception {
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(1L);
        dto.setName("Drill");

        when(itemService.getItemWithBookingsAndComments(eq(1L), eq(1L))).thenReturn(dto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getItemsByOwner_ShouldReturnList() throws Exception {
        when(itemService.getItemsWithBookingsAndCommentsByOwner(1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems_ShouldReturnList() throws Exception {
        when(itemService.searchAvailableItems("drill"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", "drill"))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() throws Exception {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Drill");

        ItemDto outputDto = new ItemDto(1L, "Updated Drill", "Powerful drill", true, null);

        when(itemService.updateItem(eq(1L), eq(1L), any(ItemDto.class))).thenReturn(outputDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Drill"));
    }

    @Test
    void addComment_ShouldReturnComment() throws Exception {
        CommentDto inputDto = new CommentDto();
        inputDto.setText("Great item!");

        CommentDto outputDto = new CommentDto();
        outputDto.setId(1L);
        outputDto.setText("Great item!");
        outputDto.setAuthorName("User");

        when(itemService.addComment(eq(1L), eq(1L), any(CommentDto.class))).thenReturn(outputDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Great item!"));
    }

    @Test
    void createItem_WithRequestId_ShouldReturnItem() throws Exception {
        ItemDto dto = new ItemDto(null, "Drill", "Powerful drill", true, 1L);
        String json = objectMapper.writeValueAsString(dto);

        when(itemService.createItem(eq(1L), any(ItemDto.class))).thenReturn(dto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById_WithBookings_ShouldReturnItemWithBookings() throws Exception {
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(1L);
        dto.setName("Drill");

        when(itemService.getItemWithBookingsAndComments(1L, 1L)).thenReturn(dto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getItemsByOwner_WithEmptyList_ShouldReturnEmpty() throws Exception {
        when(itemService.getItemsWithBookingsAndCommentsByOwner(1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems_WithSpecialCharacters_ShouldReturnOk() throws Exception {
        when(itemService.searchAvailableItems("drill&hammer")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .param("text", "drill&hammer"))
                .andExpect(status().isOk());
    }

    @Test
    void addComment_WithValidText_ShouldReturnComment() throws Exception {
        CommentDto dto = new CommentDto(null, "Great item!", null, null);
        String json = objectMapper.writeValueAsString(dto);

        when(itemService.addComment(eq(1L), eq(1L), any(CommentDto.class))).thenReturn(dto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}