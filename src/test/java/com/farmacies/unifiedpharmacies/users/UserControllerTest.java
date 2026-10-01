
package com.farmacies.unifiedpharmacies.users;

import com.farmacies.unifiedpharmacies.service.users.UserServiceImpl;
import com.farmacies.unifiedpharmacies.dto.users.UserCreateRequestDTO;
import com.farmacies.unifiedpharmacies.controller.UserController;
import com.farmacies.unifiedpharmacies.dto.users.UserResponseDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserServiceImpl userServiceImpl;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userServiceImpl))
                .build();
    }

    @Test
    void shouldFindUserById() throws Exception {

        UserResponseDTO response =
                new UserResponseDTO(
                        1,
                        "John Doe",
                        "john@email.com",
                        null,
                        UserRole.SYSTEM_ADMIN
                );

        when(userServiceImpl.findUserById(1))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@email.com"))
                .andExpect(jsonPath("$.role").value("SYSTEM_ADMIN"));

        verify(userServiceImpl).findUserById(1);
    }

    @Test
    void shouldFindAllUsers() throws Exception {

        UserResponseDTO user1 =
                new UserResponseDTO(
                        1,
                        "John Doe",
                        "john@email.com",
                        null,
                        UserRole.SYSTEM_ADMIN
                );

        UserResponseDTO user2 =
                new UserResponseDTO(
                        2,
                        "Jane Doe",
                        "jane@email.com",
                        "123456",
                        UserRole.PHARMACIST
                );

        when(userServiceImpl.findAllUsers())
                .thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(userServiceImpl).findAllUsers();
    }

    @Test
    void shouldCreateUser() throws Exception {

        UserResponseDTO response =
                new UserResponseDTO(
                        1,
                        "John Doe",
                        "john@email.com",
                        null,
                        UserRole.SYSTEM_ADMIN
                );

        when(userServiceImpl.createUser(any(UserCreateRequestDTO.class)))
                .thenReturn(response);

        String json = """
                {
                    "name": "John Doe",
                    "email": "john@email.com",
                    "password": "12345678",
                    "crfRegistration": null,
                    "role": "SYSTEM_ADMIN",
                    "pharmacyId": null
                }
                """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@email.com"))
                .andExpect(jsonPath("$.role").value("SYSTEM_ADMIN"));

        verify(userServiceImpl)
                .createUser(any(UserCreateRequestDTO.class));
    }

    @Test
    void shouldUpdateUser() throws Exception {

        UserResponseDTO response =
                new UserResponseDTO(
                        1,
                        "John Updated",
                        "john.updated@email.com",
                        null,
                        UserRole.SYSTEM_ADMIN
                );

        when(userServiceImpl.updateUser(
                eq(1),
                any(UserUpdateRequestDTO.class)))
                .thenReturn(response);

        String json = """
                {
                    "name": "John Updated",
                    "email": "john.updated@email.com",
                    "password": "12345678",
                    "crfRegistration": null,
                    "role": "SYSTEM_ADMIN",
                    "pharmacyId": null
                }
                """;

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@email.com"));

        verify(userServiceImpl)
                .updateUser(
                        eq(1),
                        any(UserUpdateRequestDTO.class)
                );
    }

    @Test
    void shouldDeleteUser() throws Exception {

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userServiceImpl).deleteUser(1);
    }
}