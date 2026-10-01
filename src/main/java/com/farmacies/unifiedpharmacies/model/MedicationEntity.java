package com.farmacies.unifiedpharmacies.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "medications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_medication_name_dosage",
                        columnNames = {"name", "dosage"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 10)
    private String dosage;

    @Column(nullable = false, length = 70)
    private String indication;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "medication")
    private List<MedicationEanEntity> eans;

    @OneToMany(mappedBy = "medication")
    private List<MedicationAvailabilityEntity> availabilities;
}




