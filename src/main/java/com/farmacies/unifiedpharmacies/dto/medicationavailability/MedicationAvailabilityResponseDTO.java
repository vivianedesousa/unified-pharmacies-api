
package com.farmacies.unifiedpharmacies.dto.medicationavailability;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationAvailabilityResponseDTO {

    private Integer id;
    private Integer pharmacyId;
    private Integer medicationId;
    private Integer quantityInformed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
