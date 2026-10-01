package com.farmacies.unifiedpharmacies.service.medicationeans;

import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanUpdateRequestDTO;

import java.util.List;

public interface MedicationEanService {

    MedicationEanResponseDTO createMedicationEan(
            MedicationEanCreateRequestDTO requestDTO
    );

    MedicationEanResponseDTO findMedicationEanById(
            Integer id
    );

    List<MedicationEanResponseDTO> findAllMedicationEans();

    MedicationEanResponseDTO updateMedicationEan(
            Integer id,
            MedicationEanUpdateRequestDTO requestDTO
    );

    void deleteMedicationEan(Integer id);
}
