package com.farmacies.unifiedpharmacies.dto.medicationavailability;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationAvailabilityCreateRequestDTO {

    @NotNull(message = "The pharmacy ID is required.")
    private Integer pharmacyId;

    @NotNull(message = "The medication ID is required.")
    private Integer medicationId;

    @NotNull(message = "The quantity informed is required.")
    @Min(
            value = 0,
            message = "The quantity informed cannot be negative."
    )
    private Integer quantityInformed;
}
