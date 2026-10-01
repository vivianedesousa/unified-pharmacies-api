package com.farmacies.unifiedpharmacies.service.prescriptions;

import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionUpdateRequestDTO;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponseDTO findPrescriptionById(Integer id);

    List<PrescriptionResponseDTO> findAllPrescriptions();

    PrescriptionResponseDTO updatePrescription(
            Integer id,
            PrescriptionUpdateRequestDTO requestDTO
    );
}
