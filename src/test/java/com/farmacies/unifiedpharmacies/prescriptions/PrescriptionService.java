package com.farmacies.unifiedpharmacies.prescriptions;

import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.PrescriptionEntity;
import com.farmacies.unifiedpharmacies.repository.PrescriptionRepository;
import com.farmacies.unifiedpharmacies.service.prescriptions.PrescriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionServiceTest {
    @Mock
    private PrescriptionRepository prescriptionRepository;
    private PrescriptionServiceImpl prescriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        prescriptionService =
                new PrescriptionServiceImpl(prescriptionRepository);
    }

    @Test
    void shouldFindPrescriptionById() {

        PrescriptionEntity prescription = new PrescriptionEntity();

        prescription.setId(1);
        prescription.setFilePath("prescription.pdf");
        prescription.setUsageInstructions("Use after meals.");
        prescription.setPharmacistComment("Approved.");
        prescription.setDurationDays(30);
        prescription.setContinuousUse(true);
        prescription.setNextRequestDate(
                LocalDate.of(2026, 10, 28)
        );

        when(prescriptionRepository.findById(1))
                .thenReturn(Optional.of(prescription));

        PrescriptionResponseDTO response =
                prescriptionService.findPrescriptionById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "prescription.pdf",
                response.getFilePath()
        );
        assertEquals(
                "Use after meals.",
                response.getUsageInstructions()
        );
        assertEquals(
                "Approved.",
                response.getPharmacistComment()
        );
        assertEquals(30, response.getDurationDays());
        assertTrue(response.getContinuousUse());
        assertEquals(
                LocalDate.of(2026, 10, 28),
                response.getNextRequestDate()
        );

        verify(prescriptionRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenPrescriptionIsNotFound() {

        when(prescriptionRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> prescriptionService.findPrescriptionById(1)
                );

        assertEquals(
                "Prescription was not found.",
                exception.getMessage()
        );

        verify(prescriptionRepository).findById(1);
    }

    @Test
    void shouldFindAllPrescriptions() {

        PrescriptionEntity prescription1 =
                new PrescriptionEntity();

        prescription1.setId(1);
        prescription1.setFilePath("prescription1.pdf");
        prescription1.setUsageInstructions("Use once a day.");
        prescription1.setDurationDays(30);
        prescription1.setContinuousUse(true);

        PrescriptionEntity prescription2 =
                new PrescriptionEntity();

        prescription2.setId(2);
        prescription2.setFilePath("prescription2.pdf");
        prescription2.setUsageInstructions("Use twice a day.");
        prescription2.setDurationDays(60);
        prescription2.setContinuousUse(false);

        when(prescriptionRepository.findAll())
                .thenReturn(List.of(prescription1, prescription2));

        List<PrescriptionResponseDTO> response =
                prescriptionService.findAllPrescriptions();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(1, response.get(0).getId());
        assertEquals(
                "prescription1.pdf",
                response.get(0).getFilePath()
        );

        assertEquals(2, response.get(1).getId());
        assertEquals(
                "prescription2.pdf",
                response.get(1).getFilePath()
        );

        verify(prescriptionRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoPrescriptions() {

        when(prescriptionRepository.findAll())
                .thenReturn(List.of());

        List<PrescriptionResponseDTO> response =
                prescriptionService.findAllPrescriptions();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(prescriptionRepository).findAll();
    }

    @Test
    void shouldUpdatePrescription() {

        PrescriptionEntity prescription =
                new PrescriptionEntity();

        prescription.setId(1);
        prescription.setFilePath("prescription.pdf");
        prescription.setUsageInstructions("Old instructions.");
        prescription.setPharmacistComment("Old comment.");
        prescription.setDurationDays(30);
        prescription.setContinuousUse(false);
        prescription.setNextRequestDate(
                LocalDate.of(2026, 10, 20)
        );

        PrescriptionUpdateRequestDTO request =
                new PrescriptionUpdateRequestDTO();

        request.setUsageInstructions("New instructions.");
        request.setPharmacistComment("New comment.");
        request.setDurationDays(60);
        request.setContinuousUse(true);
        request.setNextRequestDate(
                LocalDate.of(2026, 11, 20)
        );

        when(prescriptionRepository.findById(1))
                .thenReturn(Optional.of(prescription));

        when(prescriptionRepository.save(any(PrescriptionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PrescriptionResponseDTO response =
                prescriptionService.updatePrescription(1, request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(
                "prescription.pdf",
                response.getFilePath()
        );
        assertEquals(
                "New instructions.",
                response.getUsageInstructions()
        );
        assertEquals(
                "New comment.",
                response.getPharmacistComment()
        );
        assertEquals(60, response.getDurationDays());
        assertTrue(response.getContinuousUse());
        assertEquals(
                LocalDate.of(2026, 11, 20),
                response.getNextRequestDate()
        );

        verify(prescriptionRepository).findById(1);
        verify(prescriptionRepository).save(prescription);
    }

    @Test
    void shouldUpdateOnlyProvidedFields() {

        PrescriptionEntity prescription =
                new PrescriptionEntity();

        prescription.setId(1);
        prescription.setFilePath("prescription.pdf");
        prescription.setUsageInstructions("Original instructions.");
        prescription.setPharmacistComment("Original comment.");
        prescription.setDurationDays(30);
        prescription.setContinuousUse(true);
        prescription.setNextRequestDate(
                LocalDate.of(2026, 10, 20)
        );

        PrescriptionUpdateRequestDTO request =
                new PrescriptionUpdateRequestDTO();

        request.setDurationDays(60);

        when(prescriptionRepository.findById(1))
                .thenReturn(Optional.of(prescription));

        when(prescriptionRepository.save(any(PrescriptionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PrescriptionResponseDTO response =
                prescriptionService.updatePrescription(1, request);

        assertEquals(60, response.getDurationDays());

        assertEquals(
                "Original instructions.",
                response.getUsageInstructions()
        );

        assertEquals(
                "Original comment.",
                response.getPharmacistComment()
        );

        assertTrue(response.getContinuousUse());

        assertEquals(
                LocalDate.of(2026, 10, 20),
                response.getNextRequestDate()
        );

        verify(prescriptionRepository).save(prescription);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingPrescription() {

        PrescriptionUpdateRequestDTO request =
                new PrescriptionUpdateRequestDTO();

        request.setDurationDays(60);

        when(prescriptionRepository.findById(1))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> prescriptionService.updatePrescription(1, request)
                );

        assertEquals(
                "Prescription was not found.",
                exception.getMessage()
        );

        verify(prescriptionRepository).findById(1);

        verify(prescriptionRepository, never())
                .save(any(PrescriptionEntity.class));
    }
}
