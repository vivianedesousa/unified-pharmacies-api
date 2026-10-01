package com.farmacies.unifiedpharmacies.repository;

import com.farmacies.unifiedpharmacies.model.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {

    Optional<PatientEntity> findByCpf(String cpf);

    Optional<PatientEntity> findByEmail(String email);
}
