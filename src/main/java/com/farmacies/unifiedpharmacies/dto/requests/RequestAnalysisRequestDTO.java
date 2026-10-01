package com.farmacies.unifiedpharmacies.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAnalysisRequestDTO {

    @NotBlank(message = "The EAN is required.")
    private String ean;

    private Integer selectedPharmacyId;
}