package com.farmacies.unifiedpharmacies.service.email;

import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
import com.farmacies.unifiedpharmacies.mailtrap.MailtrapService;
import com.farmacies.unifiedpharmacies.enums.RequestStatus;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final MailtrapService mailtrapService;

    public EmailServiceImpl(MailtrapService mailtrapService) {
        this.mailtrapService = mailtrapService;
    }

    @Override
    public void sendMedicationAnalysisEmail(
            String patientName,
            String patientEmail,
            RequestStatus status,
            MedicationAvailabilityByEanResponseDTO availability) {
        String subject;
        String body;
        // Medication available at the initial pharmacy
        if (status == RequestStatus.AVAILABLE_AT_INITIAL_PHARMACY) {

            subject = "Medication available at the selected pharmacy";

            body =
                    "Hello " + patientName + ",\n\n"
                            + "Your medication request has been analyzed.\n\n"
                            + "Medication: " + availability.getMedicationName() + "\n"
                            + "Pharmacy: " + availability.getPharmacyName() + "\n"
                            + "Address: "
                            + availability.getStreet() + ", "
                            + availability.getNumber() + " - "
                            + availability.getNeighborhood() + " - "
                            + availability.getCity() + " - "
                            + availability.getState() + "\n\n"
                            + "Please pick up your medication at the selected pharmacy.";
            // Medication available at an alternative pharmacy
        } else if (status == RequestStatus.ALTERNATIVE_PHARMACY_SELECTED) {
            subject = "Medication available at another pharmacy";

            body =
                    "Hello " + patientName + ",\n\n"
                            + "Your medication request has been analyzed.\n\n"
                            + "Medication: " + availability.getMedicationName() + "\n"
                            + "Pharmacy: " + availability.getPharmacyName() + "\n"
                            + "Address: "
                            + availability.getStreet() + ", "
                            + availability.getNumber() + " - "
                            + availability.getNeighborhood() + " - "
                            + availability.getCity() + " - "
                            + availability.getState() + "\n\n"
                            + "Please pick up your medication at the selected pharmacy.";

            // Medication was not found
        } else if (status == RequestStatus.MEDICATION_NOT_FOUND) {
            subject = "Medication not available";

            body =
                    "Hello " + patientName + ",\n\n"
                            + "Your medication request has been analyzed.\n\n"
                            + "Unfortunately, the requested medication was not found "
                            + "in the available pharmacies.";

        } else {
            return;
        }

        mailtrapService.sendEmail(
                patientEmail,
                subject,
                body
        );
    }
}