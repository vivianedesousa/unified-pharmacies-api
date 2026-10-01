package com.farmacies.unifiedpharmacies.repository;

import com.farmacies.unifiedpharmacies.model.MedicationEanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicationEanRepository
        extends JpaRepository<MedicationEanEntity, Integer> {

    Optional<MedicationEanEntity> findByEan(String ean);
}
