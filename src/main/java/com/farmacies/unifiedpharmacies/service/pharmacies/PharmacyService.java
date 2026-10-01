package com.farmacies.unifiedpharmacies.service.pharmacies;

import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyResponseDTO;
import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyUpdateRequestDTO;

import java.util.List;

public interface PharmacyService {
    PharmacyResponseDTO createPharmacy(PharmacyCreateRequestDTO request);

    PharmacyResponseDTO findPharmacyById(Integer id);

    List<PharmacyResponseDTO> findAllPharmacies();

    PharmacyResponseDTO updatePharmacy(
            Integer id,
            PharmacyUpdateRequestDTO request
    );

    PharmacyResponseDTO findPharmacyByCnpj(String cnpj);

    void deactivatePharmacy(Integer id);

    PharmacyResponseDTO reactivatePharmacy(Integer id);

}
