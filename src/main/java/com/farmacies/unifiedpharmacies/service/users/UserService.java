package com.farmacies.unifiedpharmacies.service.users;

import com.farmacies.unifiedpharmacies.dto.users.UserCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserResponseDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO createUser(UserCreateRequestDTO request);

    UserResponseDTO findUserById(Integer id);

    List<UserResponseDTO> findAllUsers();

    Optional<UserEntity> findUserByEmail(String email);

    UserResponseDTO updateUser(Integer id, UserUpdateRequestDTO request);

    void deleteUser(Integer id);
}

