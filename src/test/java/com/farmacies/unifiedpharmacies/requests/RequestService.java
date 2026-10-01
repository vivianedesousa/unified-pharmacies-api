package com.farmacies.unifiedpharmacies.requests;

import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestAnalysisRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestResponseDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.enums.PharmacyStatus;
import com.farmacies.unifiedpharmacies.enums.RequestStatus;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.mailtrap.MailtrapService;
import com.farmacies.unifiedpharmacies.model.PatientEntity;
import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import com.farmacies.unifiedpharmacies.model.PrescriptionEntity;
import com.farmacies.unifiedpharmacies.model.RequestEntity;
import com.farmacies.unifiedpharmacies.repository.PatientRepository;
import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
import com.farmacies.unifiedpharmacies.repository.PrescriptionRepository;
import com.farmacies.unifiedpharmacies.repository.RequestRepository;
import com.farmacies.unifiedpharmacies.service.email.EmailService;
import com.farmacies.unifiedpharmacies.service.medicationavailability.MedicationAvailabilityService;
import com.farmacies.unifiedpharmacies.service.requests.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private MedicationAvailabilityService medicationAvailabilityService;

    @Mock
    private MailtrapService mailtrapService;

    @Mock
    private EmailService emailService;

    private RequestServiceImpl requestService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        requestService = new RequestServiceImpl(
                requestRepository,
                patientRepository,
                pharmacyRepository,
                prescriptionRepository,
                medicationAvailabilityService,
                mailtrapService,
                emailService
        );
    }

    @Test
    void shouldCreateRequest() {

        PatientEntity patient = mock(PatientEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);
        RequestEntity request = mock(RequestEntity.class);
        PrescriptionEntity prescription = mock(PrescriptionEntity.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        when(requestRepository.save(any(RequestEntity.class)))
                .thenReturn(request);

        when(request.getId())
                .thenReturn(1);

        when(prescriptionRepository.save(any(PrescriptionEntity.class)))
                .thenReturn(prescription);

        when(prescriptionRepository.findAllByRequestId(1))
                .thenReturn(List.of());

        when(request.getPatient())
                .thenReturn(patient);

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(patient.getId())
                .thenReturn(1);

        when(pharmacy.getId())
                .thenReturn(1);

        when(request.getStatus())
                .thenReturn(RequestStatus.SUBMITTED);

        PrescriptionRequestDTO prescriptionDTO =
                new PrescriptionRequestDTO(
                        "prescription.pdf",
                        "Use after meals.",
                        30,
                        true,
                        null
                );

        RequestCreateRequestDTO requestDTO =
                new RequestCreateRequestDTO(
                        1,
                        1,
                        "Test request",
                        prescriptionDTO
                );

        RequestResponseDTO response =
                requestService.createRequest(requestDTO);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getPatientId());
        assertEquals(1, response.getPharmacyId());
        assertEquals(RequestStatus.SUBMITTED, response.getStatus());

        verify(patientRepository).findById(1);
        verify(pharmacyRepository).findById(1);
        verify(requestRepository).save(any(RequestEntity.class));
        verify(prescriptionRepository).save(any(PrescriptionEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenPatientIsNotFound() {

        when(patientRepository.findById(1))
                .thenReturn(Optional.empty());

        PrescriptionRequestDTO prescriptionDTO =
                new PrescriptionRequestDTO(
                        "prescription.pdf",
                        "Use once a day.",
                        30,
                        true,
                        null
                );

        RequestCreateRequestDTO requestDTO =
                new RequestCreateRequestDTO(
                        1,
                        1,
                        "Test",
                        prescriptionDTO
                );

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> requestService.createRequest(requestDTO)
                );

        assertEquals(
                "Patient was not found.",
                exception.getMessage()
        );

        verify(patientRepository).findById(1);

        verify(requestRepository, never())
                .save(any(RequestEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenPharmacyIsNotFound() {

        PatientEntity patient = mock(PatientEntity.class);

        when(patientRepository.findById(1))
                .thenReturn(Optional.of(patient));

        when(pharmacyRepository.findById(1))
                .thenReturn(Optional.empty());

        PrescriptionRequestDTO prescriptionDTO =
                new PrescriptionRequestDTO(
                        "prescription.pdf",
                        "Use once a day.",
                        30,
                        true,
                        null
                );

        RequestCreateRequestDTO requestDTO =
                new RequestCreateRequestDTO(
                        1,
                        1,
                        "Test",
                        prescriptionDTO
                );

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> requestService.createRequest(requestDTO)
                );

        assertEquals(
                "Pharmacy was not found.",
                exception.getMessage()
        );

        verify(pharmacyRepository).findById(1);

        verify(requestRepository, never())
                .save(any(RequestEntity.class));
    }

    @Test
    void shouldFindRequestById() {

        RequestEntity request = mock(RequestEntity.class);
        PatientEntity patient = mock(PatientEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(prescriptionRepository.findAllByRequestId(1))
                .thenReturn(List.of());

        when(request.getId())
                .thenReturn(1);

        when(request.getPatient())
                .thenReturn(patient);

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(patient.getId())
                .thenReturn(1);

        when(pharmacy.getId())
                .thenReturn(1);

        when(request.getStatus())
                .thenReturn(RequestStatus.SUBMITTED);

        RequestResponseDTO response =
                requestService.findRequestById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getPatientId());
        assertEquals(1, response.getPharmacyId());
        assertEquals(
                RequestStatus.SUBMITTED,
                response.getStatus()
        );

        verify(requestRepository).findById(1);
        verify(prescriptionRepository)
                .findAllByRequestId(1);
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNotFound() {

        when(requestRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> requestService.findRequestById(1)
                );

        assertEquals(
                "Request was not found.",
                exception.getMessage()
        );

        verify(requestRepository).findById(1);
    }

    @Test
    void shouldFindAllRequests() {

        RequestEntity request = mock(RequestEntity.class);
        PatientEntity patient = mock(PatientEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(request.getId())
                .thenReturn(1);

        when(request.getPatient())
                .thenReturn(patient);

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(patient.getId())
                .thenReturn(1);

        when(pharmacy.getId())
                .thenReturn(1);

        when(request.getStatus())
                .thenReturn(RequestStatus.SUBMITTED);

        when(requestRepository.findAll())
                .thenReturn(List.of(request));

        when(prescriptionRepository.findAllByRequestId(1))
                .thenReturn(List.of());

        List<RequestResponseDTO> response =
                requestService.findAllRequests();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1, response.get(0).getId());

        verify(requestRepository).findAll();
        verify(prescriptionRepository)
                .findAllByRequestId(1);
    }

    @Test
    void shouldUpdateRequest() {

        RequestEntity request = mock(RequestEntity.class);
        PatientEntity patient = mock(PatientEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(requestRepository.save(request))
                .thenReturn(request);

        when(request.getId())
                .thenReturn(1);

        when(request.getPatient())
                .thenReturn(patient);

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(patient.getId())
                .thenReturn(1);

        when(pharmacy.getId())
                .thenReturn(1);

        when(request.getStatus())
                .thenReturn(RequestStatus.SUBMITTED);

        when(prescriptionRepository.findAllByRequestId(1))
                .thenReturn(List.of());

        RequestUpdateRequestDTO requestDTO =
                new RequestUpdateRequestDTO(
                        "Updated comment"
                );

        RequestResponseDTO response =
                requestService.updateRequest(1, requestDTO);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                RequestStatus.SUBMITTED,
                response.getStatus()
        );

        verify(requestRepository).findById(1);
        verify(requestRepository).save(request);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingRequest() {

        when(requestRepository.findById(1))
                .thenReturn(Optional.empty());

        RequestUpdateRequestDTO requestDTO =
                new RequestUpdateRequestDTO(
                        "Updated comment"
                );

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> requestService.updateRequest(1, requestDTO)
                );

        assertEquals(
                "Request was not found.",
                exception.getMessage()
        );

        verify(requestRepository).findById(1);

        verify(requestRepository, never())
                .save(any(RequestEntity.class));
    }

    @Test
    void shouldAnalyzeRequestWithInitialPharmacyAvailable() {

        RequestEntity request = mock(RequestEntity.class);
        PatientEntity patient = mock(PatientEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(pharmacy.getId())
                .thenReturn(1);

        when(request.getPatient())
                .thenReturn(patient);

        when(patient.getFullName())
                .thenReturn("Test Patient");

        when(patient.getEmail())
                .thenReturn("test@email.com");

        when(request.getId())
                .thenReturn(1);

        when(request.getStatus())
                .thenReturn(
                        RequestStatus.AVAILABLE_AT_INITIAL_PHARMACY
                );

        MedicationAvailabilityByEanResponseDTO availability =
                mock(MedicationAvailabilityByEanResponseDTO.class);

        when(availability.getPharmacyId())
                .thenReturn(1);

        when(availability.getQuantityInformed())
                .thenReturn(10);

        when(medicationAvailabilityService
                .findMedicationAvailabilityByEan("123456"))
                .thenReturn(List.of(availability));

        when(requestRepository.save(request))
                .thenReturn(request);

        when(prescriptionRepository.findAllByRequestId(1))
                .thenReturn(List.of());

        RequestAnalysisRequestDTO requestDTO =
                new RequestAnalysisRequestDTO(
                        "123456",
                        null
                );

        RequestResponseDTO response =
                requestService.analyzeRequest(1, requestDTO);

        assertNotNull(response);

        verify(requestRepository).findById(1);

        verify(medicationAvailabilityService)
                .findMedicationAvailabilityByEan("123456");

        verify(requestRepository).save(request);

        verify(emailService).sendMedicationAnalysisEmail(
                eq("Test Patient"),
                eq("test@email.com"),
                eq(RequestStatus.AVAILABLE_AT_INITIAL_PHARMACY),
                eq(availability)
        );
    }

    @Test
    void shouldSetMedicationNotFoundWhenThereIsNoAvailability() {

        RequestEntity request = mock(RequestEntity.class);
        PatientEntity patient = mock(PatientEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(pharmacy.getId())
                .thenReturn(1);

        when(request.getPatient())
                .thenReturn(patient);

        when(patient.getFullName())
                .thenReturn("Test Patient");

        when(patient.getEmail())
                .thenReturn("test@email.com");

        when(request.getId())
                .thenReturn(1);

        when(request.getStatus())
                .thenReturn(RequestStatus.MEDICATION_NOT_FOUND);

        when(medicationAvailabilityService
                .findMedicationAvailabilityByEan("123456"))
                .thenReturn(List.of());

        when(requestRepository.save(request))
                .thenReturn(request);

        when(prescriptionRepository.findAllByRequestId(1))
                .thenReturn(List.of());

        RequestAnalysisRequestDTO requestDTO =
                new RequestAnalysisRequestDTO(
                        "123456",
                        null
                );

        RequestResponseDTO response =
                requestService.analyzeRequest(1, requestDTO);

        assertNotNull(response);

        verify(requestRepository).save(request);

        verify(emailService).sendMedicationAnalysisEmail(
                eq("Test Patient"),
                eq("test@email.com"),
                eq(RequestStatus.MEDICATION_NOT_FOUND),
                isNull()
        );
    }

    @Test
    void shouldThrowExceptionWhenAlternativePharmacyIsNotSelected() {

        RequestEntity request = mock(RequestEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(pharmacy.getId())
                .thenReturn(1);

        MedicationAvailabilityByEanResponseDTO availability =
                mock(MedicationAvailabilityByEanResponseDTO.class);

        when(availability.getPharmacyId())
                .thenReturn(2);

        when(availability.getQuantityInformed())
                .thenReturn(10);

        when(medicationAvailabilityService
                .findMedicationAvailabilityByEan("123456"))
                .thenReturn(List.of(availability));

        RequestAnalysisRequestDTO requestDTO =
                new RequestAnalysisRequestDTO(
                        "123456",
                        null
                );

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> requestService.analyzeRequest(1, requestDTO)
                );

        assertEquals(
                "An alternative pharmacy must be selected.",
                exception.getMessage()
        );

        verify(requestRepository, never())
                .save(any(RequestEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenSelectedPharmacyIsInitialPharmacy() {

        RequestEntity request = mock(RequestEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(pharmacy.getId())
                .thenReturn(1);

        MedicationAvailabilityByEanResponseDTO availability =
                mock(MedicationAvailabilityByEanResponseDTO.class);

        when(availability.getPharmacyId())
                .thenReturn(2);

        when(availability.getQuantityInformed())
                .thenReturn(10);

        when(medicationAvailabilityService
                .findMedicationAvailabilityByEan("123456"))
                .thenReturn(List.of(availability));

        RequestAnalysisRequestDTO requestDTO =
                new RequestAnalysisRequestDTO(
                        "123456",
                        1
                );

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> requestService.analyzeRequest(1, requestDTO)
                );

        assertEquals(
                "The selected pharmacy must be different from the initial pharmacy.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenSelectedPharmacyDoesNotHaveMedication() {

        RequestEntity request = mock(RequestEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(pharmacy.getId())
                .thenReturn(1);

        MedicationAvailabilityByEanResponseDTO availability =
                mock(MedicationAvailabilityByEanResponseDTO.class);

        when(availability.getPharmacyId())
                .thenReturn(2);

        when(availability.getQuantityInformed())
                .thenReturn(10);

        when(medicationAvailabilityService
                .findMedicationAvailabilityByEan("123456"))
                .thenReturn(List.of(availability));

        RequestAnalysisRequestDTO requestDTO =
                new RequestAnalysisRequestDTO(
                        "123456",
                        3
                );

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> requestService.analyzeRequest(1, requestDTO)
                );

        assertEquals(
                "The selected pharmacy does not have the medication available.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenSelectedPharmacyIsNotFound() {

        RequestEntity request = mock(RequestEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(pharmacy.getId())
                .thenReturn(1);

        MedicationAvailabilityByEanResponseDTO availability =
                mock(MedicationAvailabilityByEanResponseDTO.class);

        when(availability.getPharmacyId())
                .thenReturn(2);

        when(availability.getQuantityInformed())
                .thenReturn(10);

        when(medicationAvailabilityService
                .findMedicationAvailabilityByEan("123456"))
                .thenReturn(List.of(availability));

        when(pharmacyRepository.findById(2))
                .thenReturn(Optional.empty());

        RequestAnalysisRequestDTO requestDTO =
                new RequestAnalysisRequestDTO(
                        "123456",
                        2
                );

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> requestService.analyzeRequest(1, requestDTO)
                );

        assertEquals(
                "Selected pharmacy was not found.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenSelectedPharmacyIsInactive() {

        RequestEntity request = mock(RequestEntity.class);
        PharmacyEntity pharmacy = mock(PharmacyEntity.class);
        PharmacyEntity selectedPharmacy = mock(PharmacyEntity.class);

        when(requestRepository.findById(1))
                .thenReturn(Optional.of(request));

        when(request.getPharmacy())
                .thenReturn(pharmacy);

        when(pharmacy.getId())
                .thenReturn(1);

        MedicationAvailabilityByEanResponseDTO availability =
                mock(MedicationAvailabilityByEanResponseDTO.class);

        when(availability.getPharmacyId())
                .thenReturn(2);

        when(availability.getQuantityInformed())
                .thenReturn(10);

        when(medicationAvailabilityService
                .findMedicationAvailabilityByEan("123456"))
                .thenReturn(List.of(availability));

        when(pharmacyRepository.findById(2))
                .thenReturn(Optional.of(selectedPharmacy));

        when(selectedPharmacy.getStatus())
                .thenReturn(PharmacyStatus.INACTIVE);

        RequestAnalysisRequestDTO requestDTO =
                new RequestAnalysisRequestDTO(
                        "123456",
                        2
                );

        ResourceConflictException exception =
                assertThrows(
                        ResourceConflictException.class,
                        () -> requestService.analyzeRequest(1, requestDTO)
                );

        assertEquals(
                "The selected pharmacy is inactive.",
                exception.getMessage()
        );
    }
}
