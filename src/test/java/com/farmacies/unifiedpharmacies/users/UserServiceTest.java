package com.farmacies.unifiedpharmacies.users;

import com.farmacies.unifiedpharmacies.dto.users.UserCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserResponseDTO;
import com.farmacies.unifiedpharmacies.dto.users.UserUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.enums.UserRole;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.exception.validation.BusinessValidationException;
import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import com.farmacies.unifiedpharmacies.model.UserEntity;
import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
import com.farmacies.unifiedpharmacies.repository.UserRepository;
import com.farmacies.unifiedpharmacies.service.users.PharmacistUserServiceImpl;
import com.farmacies.unifiedpharmacies.service.users.PharmacyManagerUserServiceImpl;
import com.farmacies.unifiedpharmacies.service.users.SystemAdminUserServiceImpl;
import com.farmacies.unifiedpharmacies.service.users.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserRepository userRepository;
    private PharmacyRepository pharmacyRepository;
    private SystemAdminUserServiceImpl systemAdminUserService;
    private PharmacyManagerUserServiceImpl pharmacyManagerUserService;
    private PharmacistUserServiceImpl pharmacistUserService;
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {

        userRepository = Mockito.mock(UserRepository.class);
        pharmacyRepository = Mockito.mock(PharmacyRepository.class);

        systemAdminUserService = new SystemAdminUserServiceImpl();
        pharmacyManagerUserService = new PharmacyManagerUserServiceImpl();
        pharmacistUserService = new PharmacistUserServiceImpl();

        userService = new UserServiceImpl(
                userRepository,
                pharmacyRepository,
                systemAdminUserService,
                pharmacyManagerUserService,
                pharmacistUserService
        );
    }

    @Test
    void findUserById_found() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setName("João da Silva");
        user.setEmail("joao@email.com");
        user.setCrfRegistration("12345");
        user.setRole(UserRole.PHARMACIST);
        Mockito.when(userRepository.findById(1))
                .thenReturn(Optional.of(user));
        var response = userService.findUserById(1);
        assertEquals(1, response.getId());
        assertEquals("João da Silva", response.getName());
        assertEquals("joao@email.com", response.getEmail());
        assertEquals("12345", response.getCrfRegistration());
        assertEquals(UserRole.PHARMACIST, response.getRole());

        Mockito.verify(userRepository)
                .findById(1);
    }

    @Test
    void findUserById_notFound() {

        Mockito.when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        try {
            userService.findUserById(1);
            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "User was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(userRepository)
                .findById(1);
    }

    @Test
    void findUserByEmail_found() {

        UserEntity user = new UserEntity();

        user.setId(1);
        user.setName("João da Silva");
        user.setEmail("joao@email.com");

        Mockito.when(userRepository.findByEmail("joao@email.com"))
                .thenReturn(Optional.of(user));
        Optional<UserEntity> response =
                userService.findUserByEmail("joao@email.com");

        assertTrue(response.isPresent());
        assertEquals(
                "joao@email.com",
                response.get().getEmail()
        );

        Mockito.verify(userRepository)
                .findByEmail("joao@email.com");
    }

    @Test
    void findAllUsers_success() {

        UserEntity user = new UserEntity();

        user.setId(1);
        user.setName("João da Silva");
        user.setEmail("joao@email.com");
        user.setCrfRegistration("12345");
        user.setRole(UserRole.PHARMACIST);

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user));

        var response = userService.findAllUsers();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("João da Silva", response.get(0).getName());
        assertEquals(
                "joao@email.com",
                response.get(0).getEmail()
        );

        Mockito.verify(userRepository)
                .findAll();
    }

    @Test
    void deleteUser_success() {

        UserEntity user = new UserEntity();

        user.setId(1);
        user.setName("João da Silva");

        Mockito.when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1);

        Mockito.verify(userRepository)
                .findById(1);

        Mockito.verify(userRepository)
                .delete(user);
    }

    @Test
    void deleteUser_notFound() {

        Mockito.when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            userService.deleteUser(1);
            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "User was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(userRepository)
                .findById(1);

        Mockito.verify(userRepository, Mockito.never())
                .delete(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_success() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("Administrador");
        request.setEmail("admin@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration(null);
        request.setRole(UserRole.SYSTEM_ADMIN);
        request.setPharmacyId(null);

        UserEntity savedUser = new UserEntity();

        savedUser.setId(1);
        savedUser.setName("Administrador");
        savedUser.setEmail("admin@email.com");
        savedUser.setCrfRegistration(null);
        savedUser.setRole(UserRole.SYSTEM_ADMIN);

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(savedUser);

        UserResponseDTO response =
                userService.createUser(request);

        assertEquals(1, response.getId());
        assertEquals("Administrador", response.getName());
        assertEquals("admin@email.com", response.getEmail());
        assertEquals(UserRole.SYSTEM_ADMIN, response.getRole());
        assertNull(response.getCrfRegistration());

        Mockito.verify(userRepository)
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_crfAlreadyRegistered() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("João da Silva");
        request.setEmail("joao@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration("12345");
        request.setRole(UserRole.PHARMACIST);
        request.setPharmacyId(1);

        PharmacyEntity pharmacy = new PharmacyEntity();

        pharmacy.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        UserEntity existingUser = new UserEntity();

        existingUser.setId(2);
        existingUser.setName("Outro Farmacêutico");
        existingUser.setEmail("outro@email.com");
        existingUser.setCrfRegistration("12345");
        existingUser.setRole(UserRole.PHARMACIST);

        Mockito.when(userRepository.findByCrfRegistration("12345"))
                .thenReturn(Optional.of(existingUser));

        try {

            userService.createUser(request);
            fail();

        } catch (ResourceConflictException e) {

            assertEquals(
                    "This CRF registration is already registered.",
                    e.getMessage()
            );
        }

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(userRepository)
                .findByCrfRegistration("12345");

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_pharmacyManagerAlreadyExists() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("Maria da Silva");
        request.setEmail("maria@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration(null);
        request.setRole(UserRole.PHARMACY_MANAGER);
        request.setPharmacyId(1);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        UserEntity existingManager = new UserEntity();

        existingManager.setId(2);
        existingManager.setName("João da Silva");
        existingManager.setEmail("joao@email.com");
        existingManager.setRole(UserRole.PHARMACY_MANAGER);
        existingManager.setPharmacy(pharmacy);

        Mockito.when(
                userRepository.findByPharmacyIdAndRole(
                        1,
                        UserRole.PHARMACY_MANAGER
                )
        ).thenReturn(Optional.of(existingManager));

        try {

            userService.createUser(request);
            fail();

        } catch (ResourceConflictException e) {

            assertEquals(
                    "This pharmacy already has a PHARMACY_MANAGER.",
                    e.getMessage()
            );
        }

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(userRepository)
                .findByPharmacyIdAndRole(
                        1,
                        UserRole.PHARMACY_MANAGER
                );

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_systemAdminWithPharmacy() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("Administrator");
        request.setEmail("admin@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration(null);
        request.setRole(UserRole.SYSTEM_ADMIN);
        request.setPharmacyId(1);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        try {

            userService.createUser(request);
            fail();

        } catch (RuntimeException e) {

            assertEquals(
                    "SYSTEM_ADMIN cannot be linked to a pharmacy.",
                    e.getMessage()
            );
        }

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_systemAdminWithCrf() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("Administrator");
        request.setEmail("admin@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration("12345");
        request.setRole(UserRole.SYSTEM_ADMIN);
        request.setPharmacyId(null);

        try {

            userService.createUser(request);
            fail();

        } catch (RuntimeException e) {

            assertEquals(
                    "SYSTEM_ADMIN cannot have a CRF registration.",
                    e.getMessage()
            );
        }

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_pharmacistWithoutPharmacy() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("John Pharmacist");
        request.setEmail("john@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration("12345");
        request.setRole(UserRole.PHARMACIST);
        request.setPharmacyId(null);

        try {

            userService.createUser(request);
            fail();

        } catch (RuntimeException e) {

            assertEquals(
                    "PHARMACIST must be linked to a pharmacy.",
                    e.getMessage()
            );
        }

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_pharmacistWithoutCrf() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("John Pharmacist");
        request.setEmail("john@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration(null);
        request.setRole(UserRole.PHARMACIST);
        request.setPharmacyId(1);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        try {

            userService.createUser(request);
            fail();

        } catch (BusinessValidationException e) {

            assertEquals(
                    "PHARMACIST must have a CRF registration.",
                    e.getMessage()
            );
        }

        Mockito.verify(pharmacyRepository).findById(1);

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_pharmacyManagerWithoutPharmacy() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("Pharmacy Manager");
        request.setEmail("manager@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration(null);
        request.setRole(UserRole.PHARMACY_MANAGER);
        request.setPharmacyId(null);

        try {

            userService.createUser(request);
            fail();

        } catch (BusinessValidationException e) {

            assertEquals(
                    "PHARMACY_MANAGER must be linked to a pharmacy.",
                    e.getMessage()
            );
        }

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_pharmacyManagerWithCrf() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("Pharmacy Manager");
        request.setEmail("manager@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration("12345");
        request.setRole(UserRole.PHARMACY_MANAGER);
        request.setPharmacyId(1);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        try {

            userService.createUser(request);
            fail();

        } catch (BusinessValidationException e) {

            assertEquals(
                    "PHARMACY_MANAGER cannot have a CRF registration.",
                    e.getMessage()
            );
        }

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_pharmacyManagerSuccess() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("Pharmacy Manager");
        request.setEmail("manager@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration(null);
        request.setRole(UserRole.PHARMACY_MANAGER);
        request.setPharmacyId(1);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        Mockito.when(
                userRepository.findByPharmacyIdAndRole(
                        1,
                        UserRole.PHARMACY_MANAGER
                )
        ).thenReturn(Optional.empty());

        UserEntity savedUser = new UserEntity();

        savedUser.setId(2);
        savedUser.setName("Pharmacy Manager");
        savedUser.setEmail("manager@email.com");
        savedUser.setCrfRegistration(null);
        savedUser.setRole(UserRole.PHARMACY_MANAGER);
        savedUser.setPharmacy(pharmacy);

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(savedUser);

        UserResponseDTO response = userService.createUser(request);

        assertEquals(2, response.getId());
        assertEquals("Pharmacy Manager", response.getName());
        assertEquals("manager@email.com", response.getEmail());
        assertEquals(UserRole.PHARMACY_MANAGER, response.getRole());
        assertNull(response.getCrfRegistration());

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(userRepository)
                .findByPharmacyIdAndRole(
                        1,
                        UserRole.PHARMACY_MANAGER
                );

        Mockito.verify(userRepository)
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void createUser_pharmacistSuccess() {

        UserCreateRequestDTO request = new UserCreateRequestDTO();

        request.setName("John Pharmacist");
        request.setEmail("john@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration("12345");
        request.setRole(UserRole.PHARMACIST);
        request.setPharmacyId(1);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        Mockito.when(userRepository.findByCrfRegistration("12345"))
                .thenReturn(Optional.empty());

        UserEntity savedUser = new UserEntity();

        savedUser.setId(3);
        savedUser.setName("John Pharmacist");
        savedUser.setEmail("john@email.com");
        savedUser.setCrfRegistration("12345");
        savedUser.setRole(UserRole.PHARMACIST);
        savedUser.setPharmacy(pharmacy);

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(savedUser);

        UserResponseDTO response = userService.createUser(request);

        assertEquals(3, response.getId());
        assertEquals("John Pharmacist", response.getName());
        assertEquals("john@email.com", response.getEmail());
        assertEquals("12345", response.getCrfRegistration());
        assertEquals(UserRole.PHARMACIST, response.getRole());

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(userRepository)
                .findByCrfRegistration("12345");

        Mockito.verify(userRepository)
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void updateUser_notFound() {

        UserUpdateRequestDTO request = new UserUpdateRequestDTO();

        request.setName("Updated User");
        request.setEmail("updated@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration(null);
        request.setRole(UserRole.SYSTEM_ADMIN);
        request.setPharmacyId(null);

        Mockito.when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            userService.updateUser(1, request);
            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "User was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(userRepository)
                .findById(1);

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void updateUser_success() {

        UserEntity user = new UserEntity();

        user.setId(1);
        user.setName("Old Name");
        user.setEmail("old@email.com");
        user.setPassword("12345678");
        user.setRole(UserRole.SYSTEM_ADMIN);

        Mockito.when(userRepository.findById(1))
                .thenReturn(Optional.of(user));
        UserUpdateRequestDTO request = new UserUpdateRequestDTO();
        request.setName("Updated Name");
        request.setEmail("updated@email.com");
        request.setPassword("87654321");
        request.setCrfRegistration(null);
        request.setRole(UserRole.SYSTEM_ADMIN);
        request.setPharmacyId(null);

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(user);

        UserResponseDTO response =
                userService.updateUser(1, request);

        assertEquals(1, response.getId());
        assertEquals("Updated Name", response.getName());
        assertEquals("updated@email.com", response.getEmail());
        assertEquals(UserRole.SYSTEM_ADMIN, response.getRole());
        assertNull(response.getCrfRegistration());

        Mockito.verify(userRepository)
                .findById(1);

        Mockito.verify(userRepository)
                .save(Mockito.any(UserEntity.class));
    }

    @Test
    void updateUser_roleValidation() {

        UserEntity user = new UserEntity();

        user.setId(1);
        user.setName("Administrator");
        user.setEmail("admin@email.com");
        user.setPassword("12345678");
        user.setRole(UserRole.SYSTEM_ADMIN);

        Mockito.when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        UserUpdateRequestDTO request = new UserUpdateRequestDTO();

        request.setName("Administrator");
        request.setEmail("admin@email.com");
        request.setPassword("12345678");
        request.setCrfRegistration("12345");
        request.setRole(UserRole.SYSTEM_ADMIN);
        request.setPharmacyId(null);

        try {

            userService.updateUser(1, request);
            fail();

        } catch (RuntimeException e) {

            assertEquals(
                    "SYSTEM_ADMIN cannot have a CRF registration.",
                    e.getMessage()
            );
        }

        Mockito.verify(userRepository)
                .findById(1);

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(UserEntity.class));
    }
}