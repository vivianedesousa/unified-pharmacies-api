package com.farmacies.unifiedpharmacies.dto.medications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationResponseDTO {

    private Integer id;

    private String name;

    private String dosage;

    private String indication;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
