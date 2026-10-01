package com.farmacies.unifiedpharmacies.repository;

import com.farmacies.unifiedpharmacies.model.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {
}
