package com.farmacies.unifiedpharmacies.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private RequestEntity request;

    @Column(name = "file_path", nullable = false, length = 200)
    private String filePath;

    @Column(name = "usage_instructions")
    private String usageInstructions;

    @Column(name = "pharmacist_comment")
    private String pharmacistComment;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "continuous_use", nullable = false)
    private Boolean continuousUse;

    @Column(name = "next_request_date")
    private LocalDate nextRequestDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}