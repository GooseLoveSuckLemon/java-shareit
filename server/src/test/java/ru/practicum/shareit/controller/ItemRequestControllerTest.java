package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService requestService;

    @Test
    void createRequest_ShouldReturnCreatedRequest() throws Exception {
        ItemRequestDto inputDto = new ItemRequestDto();
        inputDto.setDescription("Need a drill");

        ItemRequestDto outputDto = new ItemRequestDto();
        outputDto.setId(1L);
        outputDto.setDescription("Need a drill");
        outputDto.setCreated(LocalDateTime.now());
        outputDto.setRequestorId(1L);

        when(requestService.createRequest(eq(1L), any(ItemRequestDto.class))).thenReturn(outputDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }

    @Test
    void getOwnRequests_ShouldReturnList() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need a drill");

        when(requestService.getOwnRequests(1L)).thenReturn(List.of(requestDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Need a drill"));
    }

    @Test
    void getAllRequests_ShouldReturnPaginatedList() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need a hammer");

        when(requestService.getAllRequests(eq(1L), eq(0), eq(10))).thenReturn(List.of(requestDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Need a hammer"));
    }

    @Test
    void getRequestById_ShouldReturnRequest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Need a drill");

        when(requestService.getRequestById(1L, 1L)).thenReturn(requestDto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }

    @Test
    void createRequest_ShouldReturnCreatedRequest() throws Exception {
        ItemRequestDto inputDto = new ItemRequestDto();
        inputDto.setDescription("Need a drill");

        ItemRequestDto outputDto = new ItemRequestDto();
        outputDto.setId(1L);
        outputDto.setDescription("Need a drill");

        when(requestService.createRequest(eq(1L), any(ItemRequestDto.class))).thenReturn(outputDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getOwnRequests_ShouldReturnList() throws Exception {
        when(requestService.getOwnRequests(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_ShouldReturnList() throws Exception {
        when(requestService.getAllRequests(eq(1L), eq(0), eq(10)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }
}