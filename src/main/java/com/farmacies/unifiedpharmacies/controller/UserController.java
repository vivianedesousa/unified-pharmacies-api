package com.farmacies.unifiedpharmacies.controller;

import com.farmacies.unifiedpharmacies.dto.users.UserCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserResponseDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.service.users.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Operation(
            summary = "Get user by ID",
            description = "Endpoint responsible for retrieving a user by ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserByid(@PathVariable Integer id) {
        UserResponseDTO responseDTO = userServiceImpl.findUserById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "List all users",
            description = "Endpoint responsible for retrieving all users."
    )
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        List<UserResponseDTO> responseDTO = userServiceImpl.findAllUsers();
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Create user",
            description = "Endpoint responsible for creating a new user."
    )

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserCreateRequestDTO request) {

        UserResponseDTO responseDTO =
                userServiceImpl.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @Operation(
            summary = "Update user",
            description = "Endpoint responsible for updating an existing user."
    )

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserUpdateRequestDTO request) {

        UserResponseDTO responseDTO =
                userServiceImpl.updateUser(id, request);

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Delete user",
            description = "Endpoint responsible for deleting a user by ID."
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
