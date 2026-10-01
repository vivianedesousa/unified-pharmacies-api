package com.farmacies.unifiedpharmacies.service.medications;

import com.farmacies.unifiedpharmacies.dto.medications.MedicationCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationUpdateRequestDTO;

import java.util.List;

public interface MedicationService {

    MedicationResponseDTO createMedication(
            MedicationCreateRequestDTO requestDTO
    );

    MedicationResponseDTO findMedicationById(
            Integer id
    );

    List<MedicationResponseDTO> findAllMedications();

    MedicationResponseDTO updateMedication(
            Integer id,
            MedicationUpdateRequestDTO requestDTO
    );
}

