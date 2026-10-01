package com.farmacies.unifiedpharmacies.service.users;

import com.farmacies.unifiedpharmacies.dto.users.UserCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserResponseDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.enums.UserRole;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import com.farmacies.unifiedpharmacies.model.UserEntity;
import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
import com.farmacies.unifiedpharmacies.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;
    private final SystemAdminUserServiceImpl systemAdminUserService;
    private final PharmacyManagerUserServiceImpl pharmacyManagerUserService;
    private final PharmacistUserServiceImpl pharmacistUserService;

    public UserServiceImpl(
            UserRepository userRepository,
            PharmacyRepository pharmacyRepository,
            SystemAdminUserServiceImpl systemAdminUserService,
            PharmacyManagerUserServiceImpl pharmacyManagerUserService,
            PharmacistUserServiceImpl pharmacistUserService) {

        this.userRepository = userRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.systemAdminUserService = systemAdminUserService;
        this.pharmacyManagerUserService = pharmacyManagerUserService;
        this.pharmacistUserService = pharmacistUserService;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserCreateRequestDTO request) {
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCrfRegistration(request.getCrfRegistration());
        user.setRole(request.getRole());

        if (request.getPharmacyId() != null) {

            Optional<PharmacyEntity> pharmacy =
                    pharmacyRepository.findById(request.getPharmacyId());
            if (pharmacy.isEmpty()) {
                throw new ResourceNotFoundException("Pharmacy not found.");
            }

            user.setPharmacy(pharmacy.get());
        }

        switch (user.getRole()) {
            case SYSTEM_ADMIN -> systemAdminUserService.validate(user);
            case PHARMACY_MANAGER -> pharmacyManagerUserService.validate(user);
            case PHARMACIST -> pharmacistUserService.validate(user);
        }


        if (user.getCrfRegistration() != null) {

            Optional<UserEntity> existingUser =
                    userRepository.findByCrfRegistration(
                            user.getCrfRegistration()
                    );


            if (existingUser.isPresent()) {
                throw new ResourceConflictException(
                        "This CRF registration is already registered."
                );
            }
        }


        if (user.getRole() == UserRole.PHARMACY_MANAGER) {

            Optional<UserEntity> manager =
                    userRepository.findByPharmacyIdAndRole(
                            user.getPharmacy().getId(),
                            UserRole.PHARMACY_MANAGER
                    );


            if (manager.isPresent()) {
                throw new ResourceConflictException(
                        "This pharmacy already has a PHARMACY_MANAGER."
                );
            }
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        UserEntity saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getCrfRegistration(),
                saved.getRole()
        );
    }

    @Override
    @Transactional
    public UserResponseDTO findUserById(Integer id) {

        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User was not found.");
        }
        UserEntity saved = user.get();

        return new UserResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getCrfRegistration(),
                saved.getRole());

    }

    @Override
    @Transactional
    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        List<UserResponseDTO> responseUser = new ArrayList<>();

        for (UserEntity user : users) {

            responseUser.add(
                    new UserResponseDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getCrfRegistration(),
                            user.getRole()));

        }
        return responseUser;
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Integer id, UserUpdateRequestDTO request) {
        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User was not found.");
        }
        UserEntity saved = user.get();
        saved.setName(request.getName());
        saved.setEmail(request.getEmail());
        saved.setPassword(request.getPassword());
        saved.setCrfRegistration(request.getCrfRegistration());
        saved.setRole(request.getRole());

        if (request.getPharmacyId() != null) {

            Optional<PharmacyEntity> pharmacy =
                    pharmacyRepository.findById(request.getPharmacyId());

            if (pharmacy.isEmpty()) {
                throw new ResourceNotFoundException("Pharmacy was not found.");
            }

            saved.setPharmacy(pharmacy.get());

        }

        switch (saved.getRole()) {
            case SYSTEM_ADMIN -> systemAdminUserService.validate(saved);
            case PHARMACY_MANAGER -> pharmacyManagerUserService.validate(saved);
            case PHARMACIST -> pharmacistUserService.validate(saved);
        }

        saved.setUpdatedAt(LocalDateTime.now());
        UserEntity updated = userRepository.save(saved);

        return new UserResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getEmail(),
                updated.getCrfRegistration(),
                updated.getRole()
        );
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User was not found.");
        }
        userRepository.delete(user.get());
    }
}
