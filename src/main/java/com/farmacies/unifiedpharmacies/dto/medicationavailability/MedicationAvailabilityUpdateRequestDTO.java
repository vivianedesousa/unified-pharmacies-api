package com.farmacies.unifiedpharmacies.dto.medicationavailability;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationAvailabilityUpdateRequestDTO {

    @NotNull(message = "The quantity informed is required.")
    @Min(
            value = 0,
            message = "The quantity informed cannot be negative."
    )
    private Integer quantityInformed;
}
