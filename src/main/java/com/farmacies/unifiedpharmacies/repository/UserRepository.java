package com.farmacies.unifiedpharmacies.repository;

import com.farmacies.unifiedpharmacies.enums.UserRole;
import com.farmacies.unifiedpharmacies.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByPharmacyIdAndRole(
            Integer pharmacyId,
            UserRole role
    );

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByCrfRegistration(String crfRegistration);

}
