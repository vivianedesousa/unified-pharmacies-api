package com.farmacies.unifiedpharmacies.dto.medicationeans;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationEanResponseDTO {

    private Integer id;
    private Integer medicationId;
    private String ean;
}
