package com.farmacies.unifiedpharmacies.repository;

import com.farmacies.unifiedpharmacies.model.PrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, Integer> {

    List<PrescriptionEntity> findAllByRequestId(Integer requestId);
}
