
package com.farmacies.unifiedpharmacies.service.medicationavailability;

import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;

import java.util.List;

public interface MedicationAvailabilityService {
    MedicationAvailabilityResponseDTO createMedicationAvailability(
            MedicationAvailabilityCreateRequestDTO requestDTO
    );

    MedicationAvailabilityResponseDTO findMedicationAvailabilityById(
            Integer id
    );

    List<MedicationAvailabilityResponseDTO> findAllMedicationAvailabilities();

    MedicationAvailabilityResponseDTO updateMedicationAvailability(
            Integer id,
            MedicationAvailabilityUpdateRequestDTO requestDTO
    );

    void deleteMedicationAvailability(
            Integer id
    );

    List<MedicationAvailabilityByEanResponseDTO> findMedicationAvailabilityByEan(
            String ean
    );
}
