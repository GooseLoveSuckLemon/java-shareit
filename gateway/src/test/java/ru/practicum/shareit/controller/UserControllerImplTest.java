package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.user.controller.UserControllerImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserControllerImpl.class)
class UserControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @Test
    void createUser_WithValidData_ShouldReturnOk() throws Exception {
        UserDto dto = new UserDto(null, "John Doe", "john@example.com");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/users/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_ShouldReturnOk() throws Exception {
        UserDto dto = new UserDto(null, "Updated Name", null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_WithNameOnly_ShouldReturnOk() throws Exception {
        UserDto dto = new UserDto(null, "Updated Name", null);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_WithEmailOnly_ShouldReturnOk() throws Exception {
        UserDto dto = new UserDto(null, null, "newemail@example.com");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_WithEmptyBody_ShouldReturnOk() throws Exception {
        String json = "{}";

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        List<UserDto> users = Collections.singletonList(new UserDto(1L, "John", "john@test.com"));
        when(userClient.getAllUsers()).thenReturn(ResponseEntity.ok(users));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getAllUsers_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        when(userClient.getAllUsers()).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserDto user = new UserDto(1L, "John", "john@test.com");
        when(userClient.getUserById(1L)).thenReturn(ResponseEntity.ok(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        UserDto input = new UserDto(null, "New User", "new@test.com");
        UserDto output = new UserDto(1L, "New User", "new@test.com");
        when(userClient.createUser(any(UserDto.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(output));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateUser_ShouldReturnUpdated() throws Exception {
        UserDto input = new UserDto(null, "Updated Name", null);
        UserDto output = new UserDto(1L, "Updated Name", "john@test.com");
        when(userClient.updateUser(eq(1L), any(UserDto.class))).thenReturn(ResponseEntity.ok(output));

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void updateUser_WithEmail_ShouldReturnUpdated() throws Exception {
        UserDto input = new UserDto(null, null, "newemail@test.com");
        UserDto output = new UserDto(1L, "John", "newemail@test.com");
        when(userClient.updateUser(eq(1L), any(UserDto.class))).thenReturn(ResponseEntity.ok(output));

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newemail@test.com"));
    }
}