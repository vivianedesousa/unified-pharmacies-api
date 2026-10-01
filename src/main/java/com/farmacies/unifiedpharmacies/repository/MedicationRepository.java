package com.farmacies.unifiedpharmacies.repository;

import com.farmacies.unifiedpharmacies.model.MedicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<MedicationEntity, Integer> {

    Optional<MedicationEntity> findByNameAndDosage(
            String name,
            String dosage
    );
}

