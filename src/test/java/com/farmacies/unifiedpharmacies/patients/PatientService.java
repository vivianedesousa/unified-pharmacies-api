package com.farmacies.unifiedpharmacies.patients;

import com.farmacies.unifiedpharmacies.dto.patients.*;
import com.farmacies.unifiedpharmacies.enums.PatientStatus;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.PatientEntity;
import com.farmacies.unifiedpharmacies.repository.PatientRepository;
import com.farmacies.unifiedpharmacies.service.patients.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patientService =
                new PatientServiceImpl(patientRepository);
    }

    private PatientCreateRequestDTO createRequest() {
        return new PatientCreateRequestDTO(
                "Maria da Silva",
                "123.456.789-00",
                "maria@email.com",
                "27999999999",
                "password123",
                "29160-000",
                "ES",
                "Serra",
                "Centro",
                "Rua Principal",
                "100",
                "Apto 10"
        );
    }

    @Test
    void shouldCreatePatient() {

        PatientCreateRequestDTO request =
                createRequest();

        PatientEntity saved = new PatientEntity();

        saved.setId(1);
        saved.setFullName("Maria da Silva");
        saved.setCpf("123.456.789-00");
        saved.setEmail("maria@email.com");
        saved.setPhone("27999999999");
        saved.setPassword("password123");
        saved.setZipCode("29160-000");
        saved.setState("ES");
        saved.setCity("Serra");
        saved.setNeighborhood("Downtown");
        saved.setStreet("Main Street");
        saved.setNumber("100");
        saved.setComplement("Apartment 10");
        saved.setStatus(PatientStatus.ACTIVE);
        when(patientRepository.findByCpf(request.getCpf()))
                .thenReturn(Optional.empty());

        when(patientRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(patientRepository.save(any(PatientEntity.class)))
                .thenReturn(saved);

        PatientResponseDTO response =
                patientService.createPatient(request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Maria da Silva", response.getFullName());
        assertEquals(
                "123.456.789-00",
                response.getCpf()
        );
        assertEquals(
                "maria@email.com",
                response.getEmail()
        );
        assertEquals(
                PatientStatus.ACTIVE,
                response.getStatus()
        );

        verify(patientRepository)
                .findByCpf(request.getCpf());

        verify(patientRepository)
                .findByEmail(request.getEmail());

        verify(patientRepository)
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {

        PatientEntity existing =
                new PatientEntity();

        existing.setId(10);
        existing.setCpf("123.456.789-00");

        PatientCreateRequestDTO request =
                createRequest();

        when(patientRepository.findByCpf(request.getCpf()))
                .thenReturn(Optional.of(existing));

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> patientService.createPatient(request)
                );

        assertEquals(
                "A patient with this CPF already exists.",
                exception.getMessage()
        );

        verify(patientRepository)
                .findByCpf(request.getCpf());

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        PatientEntity existing =
                new PatientEntity();

        existing.setId(10);
        existing.setEmail("maria@email.com");

        PatientCreateRequestDTO request =
                createRequest();

        when(patientRepository.findByCpf(request.getCpf()))
                .thenReturn(Optional.empty());

        when(patientRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(existing));

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> patientService.createPatient(request)
                );

        assertEquals(
                "A patient with this email already exists.",
                exception.getMessage()
        );

        verify(patientRepository)
                .findByCpf(request.getCpf());

        verify(patientRepository)
                .findByEmail(request.getEmail());

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldFindPatientById() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setFullName("Maria da Silva");
        patient.setCpf("123.456.789-00");
        patient.setEmail("maria@email.com");
        patient.setStatus(PatientStatus.ACTIVE);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        PatientResponseDTO response =
                patientService.findPatientById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "Maria da Silva",
                response.getFullName()
        );
        assertEquals(
                "123.456.789-00",
                response.getCpf()
        );
        assertEquals(
                PatientStatus.ACTIVE,
                response.getStatus()
        );

        verify(patientRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenPatientIsNotFound() {

        when(patientRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> patientService.findPatientById(1)
                );

        assertEquals(
                "Patient was not found.",
                exception.getMessage()
        );

        verify(patientRepository).findById(1);
    }

    @Test
    void shouldFindAllPatients() {

        PatientEntity patient1 =
                new PatientEntity();

        patient1.setId(1);
        patient1.setFullName("Maria");
        patient1.setCpf("111.111.111-11");
        patient1.setEmail("maria@email.com");
        patient1.setStatus(PatientStatus.ACTIVE);

        PatientEntity patient2 =
                new PatientEntity();

        patient2.setId(2);
        patient2.setFullName("Joao");
        patient2.setCpf("222.222.222-22");
        patient2.setEmail("joao@email.com");
        patient2.setStatus(PatientStatus.INACTIVE);

        when(patientRepository.findAll())
                .thenReturn(List.of(patient1, patient2));

        List<PatientResponseDTO> response =
                patientService.findAllPatients();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(1, response.get(0).getId());
        assertEquals(
                "Maria",
                response.get(0).getFullName()
        );

        assertEquals(2, response.get(1).getId());
        assertEquals(
                "Joao",
                response.get(1).getFullName()
        );

        verify(patientRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoPatients() {

        when(patientRepository.findAll())
                .thenReturn(List.of());

        List<PatientResponseDTO> response =
                patientService.findAllPatients();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(patientRepository).findAll();
    }

    @Test
    void shouldUpdatePatient() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setFullName("Maria");
        patient.setEmail("old@email.com");
        patient.setPhone("27999999999");
        patient.setStatus(PatientStatus.ACTIVE);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patientRepository.findByEmail("new@email.com"))
                .thenReturn(Optional.empty());

        when(patientRepository.save(patient))
                .thenReturn(patient);

        PatientUpdateRequestDTO request =
                new PatientUpdateRequestDTO(
                        "Maria da Silva",
                        "new@email.com",
                        "27888888888",
                        "newpassword123",
                        "29160-000",
                        "ES",
                        "Serra",
                        "Downtown",
                        "New Street",
                        "200",
                        "Apartment 20"
                );

        PatientResponseDTO response =
                patientService.updatePatient(1, request);

        assertNotNull(response);
        assertEquals(1, response.getId());

        verify(patientRepository)
                .findById(1);

        verify(patientRepository)
                .findByEmail("new@email.com");

        verify(patientRepository)
                .save(patient);
    }

    @Test
    void shouldNotChangePasswordWhenPasswordIsNull() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setPassword("oldpassword");
        patient.setEmail("old@email.com");
        patient.setStatus(PatientStatus.ACTIVE);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patientRepository.findByEmail("new@email.com"))
                .thenReturn(Optional.empty());

        when(patientRepository.save(patient))
                .thenReturn(patient);

        PatientUpdateRequestDTO request =
                new PatientUpdateRequestDTO(
                        "Maria da Silva",
                        "new@email.com",
                        "27888888888",
                        null,
                        "29160-000",
                        "ES",
                        "Serra",
                        "Centro",
                        "Rua Nova",
                        "200",
                        null
                );

        patientService.updatePatient(1, request);

        assertEquals(
                "oldpassword",
                patient.getPassword()
        );

        verify(patientRepository).save(patient);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingPatient() {

        when(patientRepository.findById(1))
                .thenReturn(Optional.empty());

        PatientUpdateRequestDTO request =
                new PatientUpdateRequestDTO(
                        "Maria",
                        "maria@email.com",
                        "27999999999",
                        null,
                        "29160-000",
                        "ES",
                        "Serra",
                        "Downtown",
                        "New Street",
                        "2010",
                        null
                );

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> patientService.updatePatient(1, request)
                );

        assertEquals(
                "Patient was not found.",
                exception.getMessage()
        );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);

        PatientEntity anotherPatient =
                new PatientEntity();

        anotherPatient.setId(2);
        anotherPatient.setEmail("existing@email.com");

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patientRepository.findByEmail("existing@email.com"))
                .thenReturn(Optional.of(anotherPatient));

        PatientUpdateRequestDTO request =
                new PatientUpdateRequestDTO(
                        "Maria",
                        "existing@email.com",
                        "27999999999",
                        null,
                        "29160-000",
                        "ES",
                        "Serra",
                        "Downtown",
                        "New Street",
                        "200",
                        null
                );

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> patientService.updatePatient(1, request)
                );

        assertEquals(
                "A patient with this email already exists.",
                exception.getMessage()
        );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldFindPatientStatus() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setStatus(PatientStatus.ACTIVE);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        PatientStatusResponseDTO response =
                patientService.findPatientStatus(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                PatientStatus.ACTIVE,
                response.getStatus()
        );

        verify(patientRepository).findById(1);
    }

    @Test
    void shouldUpdatePatientCpf() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setFullName("Maria");
        patient.setCpf("111.111.111-11");

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patientRepository.findByCpf("222.222.222-22"))
                .thenReturn(Optional.empty());

        when(patientRepository.save(patient))
                .thenReturn(patient);

        PatientCpfUpdateRequestDTO request =
                new PatientCpfUpdateRequestDTO(
                        "222.222.222-22"
                );

        PatientCpfResponseDTO response =
                patientService.updatePatientCpf(1, request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "Maria",
                response.getFullName()
        );
        assertEquals(
                "222.222.222-22",
                response.getCpf()
        );

        verify(patientRepository).findById(1);
        verify(patientRepository)
                .findByCpf("222.222.222-22");
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldThrowExceptionWhenCpfBelongsToAnotherPatient() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);

        PatientEntity anotherPatient =
                new PatientEntity();

        anotherPatient.setId(2);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(patientRepository.findByCpf("222.222.222-22"))
                .thenReturn(Optional.of(anotherPatient));

        PatientCpfUpdateRequestDTO request =
                new PatientCpfUpdateRequestDTO(
                        "222.222.222-22"
                );

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> patientService.updatePatientCpf(1, request)
                );

        assertEquals(
                "A patient with this CPF already exists.",
                exception.getMessage()
        );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldDeactivatePatient() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setStatus(PatientStatus.ACTIVE);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        patientService.deactivatePatient(1);

        assertEquals(
                PatientStatus.INACTIVE,
                patient.getStatus()
        );

        verify(patientRepository).findById(1);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldThrowExceptionWhenPatientIsAlreadyInactive() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setStatus(PatientStatus.INACTIVE);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> patientService.deactivatePatient(1)
                );

        assertEquals(
                "Patient is already inactive.",
                exception.getMessage()
        );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldReactivatePatient() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setStatus(PatientStatus.INACTIVE);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        patientService.reactivatePatient(1);

        assertEquals(
                PatientStatus.ACTIVE,
                patient.getStatus()
        );

        verify(patientRepository).findById(1);
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldThrowExceptionWhenPatientIsAlreadyActive() {

        PatientEntity patient =
                new PatientEntity();

        patient.setId(1);
        patient.setStatus(PatientStatus.ACTIVE);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> patientService.reactivatePatient(1)
                );

        assertEquals(
                "Patient is already active.",
                exception.getMessage()
        );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingNonExistingPatient() {

        when(patientRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> patientService.deactivatePatient(1)
                );

        assertEquals(
                "Patient was not found.",
                exception.getMessage()
        );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenReactivatingNonExistingPatient() {

        when(patientRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> patientService.reactivatePatient(1)
                );

        assertEquals(
                "Patient was not found.",
                exception.getMessage()
        );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }
}