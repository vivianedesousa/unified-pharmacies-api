package com.farmacies.unifiedpharmacies.service.email;

import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
import com.farmacies.unifiedpharmacies.enums.RequestStatus;

public interface EmailService {

    void sendMedicationAnalysisEmail(
            String patientName,
            String patientEmail,
            RequestStatus status,
            MedicationAvailabilityByEanResponseDTO availability
    );
}