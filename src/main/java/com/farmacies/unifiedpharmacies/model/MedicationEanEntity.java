package com.farmacies.unifiedpharmacies.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medication_eans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationEanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private MedicationEntity medication;

    @Column(nullable = false, unique = true, length = 14)
    private String ean;
}