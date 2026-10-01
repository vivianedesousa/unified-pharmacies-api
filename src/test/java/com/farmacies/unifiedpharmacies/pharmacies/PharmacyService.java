package com.farmacies.unifiedpharmacies.pharmacies;

import com.farmacies.unifiedpharmacies.dto.pharmacies.*;
import com.farmacies.unifiedpharmacies.enums.PharmacyStatus;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
import com.farmacies.unifiedpharmacies.service.pharmacies.PharmacyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PharmacyServiceTest {

    @Mock
    private PharmacyRepository pharmacyRepository;

    private PharmacyServiceImpl pharmacyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pharmacyService =
                new PharmacyServiceImpl(pharmacyRepository);
    }

    private PharmacyCreateRequestDTO createRequest() {
        return new PharmacyCreateRequestDTO(
                "12.345.678/0001-90",
                "Legal Pharmacy LTD",
                "Legal Pharmacy",
                "pharmacy@email.com",
                "2733333333",
                "29160-000",
                "ES",
                "Serra",
                "Centro",
                "Rua Principal",
                "100",
                "Loja 1"
        );
    }

    @Test
    void shouldCreatePharmacy() {

        PharmacyCreateRequestDTO request =
                createRequest();

        PharmacyEntity saved =
                new PharmacyEntity();
        saved.setId(1);
        saved.setCnpj("12.345.678/0001-90");
        saved.setLegalName("Farmácia Legal LTDA");
        saved.setTradeName("Farmácia Legal");
        saved.setEmail("farmacia@email.com");
        saved.setPhone("2733333333");
        saved.setZipCode("29160-000");
        saved.setState("ES");
        saved.setCity("Serra");
        saved.setNeighborhood("Centro");
        saved.setStreet("Rua Principal");
        saved.setNumber("100");
        saved.setComplement("Loja 1");
        saved.setStatus(PharmacyStatus.ACTIVE);

        when(pharmacyRepository.findByCnpj(request.getCnpj()))
                .thenReturn(Optional.empty());

        when(pharmacyRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(pharmacyRepository.save(any(PharmacyEntity.class)))
                .thenReturn(saved);

        PharmacyResponseDTO response =
                pharmacyService.createPharmacy(request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "12.345.678/0001-90",
                response.getCnpj()
        );
        assertEquals(
                "Farmacia Legal",
                response.getTradeName()
        );
        assertEquals(
                "farmacia@email.com",
                response.getEmail()
        );

        verify(pharmacyRepository)
                .findByCnpj(request.getCnpj());

        verify(pharmacyRepository)
                .findByEmail(request.getEmail());

        verify(pharmacyRepository)
                .save(any(PharmacyEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenCnpjAlreadyExists() {

        PharmacyEntity existing =
                new PharmacyEntity();

        existing.setId(10);
        existing.setCnpj("12.345.678/0001-90");

        PharmacyCreateRequestDTO request =
                createRequest();

        when(pharmacyRepository.findByCnpj(request.getCnpj()))
                .thenReturn(Optional.of(existing));

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> pharmacyService.createPharmacy(request)
                );

        assertEquals(
                "A pharmacy with this CNPJ already exists.",
                exception.getMessage()
        );

        verify(pharmacyRepository)
                .findByCnpj(request.getCnpj());

        verify(pharmacyRepository, never())
                .save(any(PharmacyEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        PharmacyEntity existing =
                new PharmacyEntity();

        existing.setId(10);
        existing.setEmail("farmacia@email.com");

        PharmacyCreateRequestDTO request =
                createRequest();

        when(pharmacyRepository.findByCnpj(request.getCnpj()))
                .thenReturn(Optional.empty());

        when(pharmacyRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(existing));

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> pharmacyService.createPharmacy(request)
                );

        assertEquals(
                "A pharmacy with this email already exists.",
                exception.getMessage()
        );

        verify(pharmacyRepository)
                .findByEmail(request.getEmail());

        verify(pharmacyRepository, never())
                .save(any(PharmacyEntity.class));
    }

    @Test
    void shouldFindPharmacyById() {

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);
        pharmacy.setCnpj("12.345.678/0001-90");
        pharmacy.setLegalName("Legal Pharmacy LTD");
        pharmacy.setTradeName("Legal Pharmacy");
        pharmacy.setEmail("pharmacy@email.com");
        pharmacy.setStatus(PharmacyStatus.ACTIVE);

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        PharmacyResponseDTO response =
                pharmacyService.findPharmacyById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "12.345.678/0001-90",
                response.getCnpj()
        );
        assertEquals(
                PharmacyStatus.ACTIVE,
                response.getStatus()
        );

        verify(pharmacyRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenPharmacyIsNotFound() {

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> pharmacyService.findPharmacyById(1)
                );

        assertEquals(
                "Pharmacy was not found.",
                exception.getMessage()
        );

        verify(pharmacyRepository).findById(1);
    }

    @Test
    void shouldFindAllPharmacies() {

        PharmacyEntity pharmacy1 =
                new PharmacyEntity();

        pharmacy1.setId(1);
        pharmacy1.setTradeName("Farmacia A");
        pharmacy1.setCnpj("11.111.111/0001-11");
        pharmacy1.setStatus(PharmacyStatus.ACTIVE);

        PharmacyEntity pharmacy2 =
                new PharmacyEntity();

        pharmacy2.setId(2);
        pharmacy2.setTradeName("Farmacia B");
        pharmacy2.setCnpj("22.222.222/0001-22");
        pharmacy2.setStatus(PharmacyStatus.INACTIVE);

        when(pharmacyRepository.findAll())
                .thenReturn(List.of(pharmacy1, pharmacy2));

        List<PharmacyResponseDTO> response =
                pharmacyService.findAllPharmacies();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(1, response.get(0).getId());
        assertEquals(
                "Farmacia A",
                response.get(0).getTradeName()
        );

        assertEquals(2, response.get(1).getId());
        assertEquals(
                "Farmacia B",
                response.get(1).getTradeName()
        );

        verify(pharmacyRepository).findAll();
    }

    @Test
    void shouldUpdatePharmacy() {

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);
        pharmacy.setCnpj("12.345.678/0001-90");
        pharmacy.setEmail("old@email.com");
        pharmacy.setStatus(PharmacyStatus.ACTIVE);

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        when(pharmacyRepository.findByEmail("new@email.com"))
                .thenReturn(Optional.empty());

        when(pharmacyRepository.save(pharmacy))
                .thenReturn(pharmacy);

        PharmacyUpdateRequestDTO request =
                new PharmacyUpdateRequestDTO(
                        "New Legal Name",
                        "New Pharmacy",
                        "new@email.com",
                        "27999999999",
                        "29160-000",
                        "ES",
                        "Serra",
                        "Downtown",
                        "New Street",
                        "200",
                        "Store 2"
                );

        PharmacyResponseDTO response =
                pharmacyService.updatePharmacy(1, request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "Nova Farmacia",
                response.getTradeName()
        );
        assertEquals(
                "new@email.com",
                response.getEmail()
        );

        verify(pharmacyRepository).findById(1);
        verify(pharmacyRepository)
                .findByEmail("new@email.com");
        verify(pharmacyRepository)
                .save(pharmacy);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingPharmacy() {

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.empty());

        PharmacyUpdateRequestDTO request =
                new PharmacyUpdateRequestDTO(
                        "New Name",
                        "New Pharmacy",
                        "new@email.com",
                        "27999999999",
                        "29160-000",
                        "ES",
                        "Serra",
                        "Downtown",
                        "New Street",
                        "200",
                        null
                );

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> pharmacyService.updatePharmacy(1, request)
                );

        assertEquals(
                "Pharmacy was not found.",
                exception.getMessage()
        );

        verify(pharmacyRepository, never())
                .save(any(PharmacyEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);

        PharmacyEntity anotherPharmacy =
                new PharmacyEntity();

        anotherPharmacy.setId(2);
        anotherPharmacy.setEmail("existing@email.com");

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        when(pharmacyRepository.findByEmail("existing@email.com"))
                .thenReturn(Optional.of(anotherPharmacy));

        PharmacyUpdateRequestDTO request =
                new PharmacyUpdateRequestDTO(
                        "Legal Name",
                        "Pharmacy",
                        "existing@email.com",
                        "27999999999",
                        "29160-000",
                        "ES",
                        "Serra",
                        "Downtown",
                        "Street",
                        "100",
                        null
                );

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> pharmacyService.updatePharmacy(1, request)
                );

        assertEquals(
                "A pharmacy with this email already exists.",
                exception.getMessage()
        );

        verify(pharmacyRepository, never())
                .save(any(PharmacyEntity.class));
    }

    @Test
    void shouldFindPharmacyByCnpj() {

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);
        pharmacy.setCnpj("12.345.678/0001-90");
        pharmacy.setTradeName("Farmacia Legal");
        pharmacy.setStatus(PharmacyStatus.ACTIVE);

        when(pharmacyRepository
                .findByCnpj("12.345.678/0001-90"))
                .thenReturn(Optional.of(pharmacy));

        PharmacyResponseDTO response =
                pharmacyService.findPharmacyByCnpj(
                        "12.345.678/0001-90"
                );

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "12.345.678/0001-90",
                response.getCnpj()
        );

        verify(pharmacyRepository)
                .findByCnpj("12.345.678/0001-90");
    }

    @Test
    void shouldThrowExceptionWhenCnpjIsNotFound() {

        when(pharmacyRepository
                .findByCnpj("12.345.678/0001-90"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> pharmacyService.findPharmacyByCnpj(
                                "12.345.678/0001-90"
                        )
                );

        assertEquals(
                "Pharmacy was not found.",
                exception.getMessage()
        );
    }

    @Test
    void shouldDeactivatePharmacy() {

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);
        pharmacy.setStatus(PharmacyStatus.ACTIVE);

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        pharmacyService.deactivatePharmacy(1);

        assertEquals(
                PharmacyStatus.INACTIVE,
                pharmacy.getStatus()
        );

        verify(pharmacyRepository).findById(1);
        verify(pharmacyRepository).save(pharmacy);
    }

    @Test
    void shouldThrowExceptionWhenPharmacyIsAlreadyInactive() {

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);
        pharmacy.setStatus(PharmacyStatus.INACTIVE);

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> pharmacyService.deactivatePharmacy(1)
                );

        assertEquals(
                "Pharmacy is already inactive.",
                exception.getMessage()
        );

        verify(pharmacyRepository, never())
                .save(any(PharmacyEntity.class));
    }

    @Test
    void shouldReactivatePharmacy() {

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);
        pharmacy.setCnpj("12.345.678/0001-90");
        pharmacy.setLegalName("Legal Pharmacy LTD");
        pharmacy.setTradeName("Legal Pharmacy");
        pharmacy.setEmail("pharmacy@email.com");
        pharmacy.setStatus(PharmacyStatus.INACTIVE);

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        when(pharmacyRepository.save(pharmacy))
                .thenReturn(pharmacy);

        PharmacyResponseDTO response =
                pharmacyService.reactivatePharmacy(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                PharmacyStatus.ACTIVE,
                response.getStatus()
        );

        verify(pharmacyRepository).findById(1);
        verify(pharmacyRepository).save(pharmacy);
    }

    @Test
    void shouldThrowExceptionWhenPharmacyIsAlreadyActive() {

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);
        pharmacy.setStatus(PharmacyStatus.ACTIVE);

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> pharmacyService.reactivatePharmacy(1)
                );

        assertEquals(
                "Pharmacy is already active.",
                exception.getMessage()
        );

        verify(pharmacyRepository, never())
                .save(any(PharmacyEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingNonExistingPharmacy() {

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> pharmacyService.deactivatePharmacy(1)
                );

        assertEquals(
                "Pharmacy was not found.",
                exception.getMessage()
        );

        verify(pharmacyRepository, never())
                .save(any(PharmacyEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenReactivatingNonExistingPharmacy() {

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> pharmacyService.reactivatePharmacy(1)
                );

        assertEquals(
                "Pharmacy was not found.",
                exception.getMessage()
        );

        verify(pharmacyRepository, never())
                .save(any(PharmacyEntity.class));
    }
}