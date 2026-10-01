package com.farmacies.unifiedpharmacies.email;

import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
import com.farmacies.unifiedpharmacies.enums.RequestStatus;
import com.farmacies.unifiedpharmacies.mailtrap.MailtrapService;
import com.farmacies.unifiedpharmacies.service.email.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    private MailtrapService mailtrapService;

    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        emailService = new EmailServiceImpl(mailtrapService);
    }

    @Test
    void shouldSendEmailWhenMedicationIsAvailableAtInitialPharmacy() {

        MedicationAvailabilityByEanResponseDTO availability =
                new MedicationAvailabilityByEanResponseDTO();

        availability.setMedicationName("Metformin");
        availability.setPharmacyName("Legal Pharmacy");
        availability.setStreet("Main Street");
        availability.setNumber("100");
        availability.setNeighborhood("Centro");
        availability.setCity("Serra");
        availability.setState("ES");

        emailService.sendMedicationAnalysisEmail(
                "John Doe",
                "john@email.com",
                RequestStatus.AVAILABLE_AT_INITIAL_PHARMACY,
                availability
        );

        verify(mailtrapService).sendEmail(
                eq("john@email.com"),
                eq("Medication available at the selected pharmacy"),
                contains("Hello John Doe")
        );
    }

    @Test
    void shouldSendEmailWhenAlternativePharmacyIsSelected() {

        MedicationAvailabilityByEanResponseDTO availability =
                new MedicationAvailabilityByEanResponseDTO();

        availability.setMedicationName("Metformin");
        availability.setPharmacyName("Alternative Pharmacy");
        availability.setStreet("New Street");
        availability.setNumber("200");
        availability.setNeighborhood("Centro");
        availability.setCity("Serra");
        availability.setState("ES");

        emailService.sendMedicationAnalysisEmail(
                "John Doe",
                "john@email.com",
                RequestStatus.ALTERNATIVE_PHARMACY_SELECTED,
                availability
        );

        verify(mailtrapService).sendEmail(
                eq("john@email.com"),
                eq("Medication available at another pharmacy"),
                contains("Medication: Metformin")
        );
    }

    @Test
    void shouldSendEmailWhenMedicationIsNotFound() {

        emailService.sendMedicationAnalysisEmail(
                "John Doe",
                "john@email.com",
                RequestStatus.MEDICATION_NOT_FOUND,
                null
        );

        verify(mailtrapService).sendEmail(
                eq("john@email.com"),
                eq("Medication not available"),
                contains("Unfortunately, the requested medication was not found")
        );
    }

    @Test
    void shouldNotSendEmailForOtherStatuses() {

        emailService.sendMedicationAnalysisEmail(
                "John Doe",
                "john@email.com",
                RequestStatus.SUBMITTED,
                null
        );

        verify(mailtrapService, never())
                .sendEmail(
                        anyString(),
                        anyString(),
                        anyString()
                );
    }
}