package com.farmacies.unifiedpharmacies.repository;

import com.farmacies.unifiedpharmacies.model.MedicationAvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicationAvailabilityRepository extends JpaRepository<MedicationAvailabilityEntity, Integer> {
    Optional<MedicationAvailabilityEntity> findByPharmacyIdAndMedicationId(
            Integer pharmacyId,
            Integer medicationId
    );

    List<MedicationAvailabilityEntity> findAllByMedicationId(
            Integer medicationId
    );
}
