package com.farmacies.unifiedpharmacies.repository;

import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PharmacyRepository extends JpaRepository<PharmacyEntity, Integer> {
    Optional<PharmacyEntity> findByCnpj(String cnpj);

    Optional<PharmacyEntity> findByEmail(String email);
}
