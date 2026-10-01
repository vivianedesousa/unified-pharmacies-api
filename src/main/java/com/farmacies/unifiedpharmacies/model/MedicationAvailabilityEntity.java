package com.farmacies.unifiedpharmacies.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "medication_availability",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_availability_pharmacy_medication",
                        columnNames = {"pharmacy_id", "medication_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationAvailabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private MedicationEntity medication;

    @Column(name = "quantity_informed", nullable = false)
    private Integer quantityInformed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private PharmacyEntity pharmacy;
}