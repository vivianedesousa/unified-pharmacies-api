package com.farmacies.unifiedpharmacies.dto.medicationeans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MedicationEanCreateRequestDTO {
    @NotNull(message = "The medication ID is required.")
    private Integer medicationId;

    @NotBlank(message = "The EAN is required.")
    @Size(
            max = 14,
            message = "The EAN must contain at most 14 characters."
    )
    private String ean;
}
