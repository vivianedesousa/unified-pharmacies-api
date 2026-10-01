package com.farmacies.unifiedpharmacies.dto.prescriptions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequestDTO {

    @NotBlank(message = "The file path is required.")
    private String filePath;

    private String usageInstructions;

    private Integer durationDays;

    @NotNull(message = "The continuous use information is required.")
    private Boolean continuousUse;

    private LocalDate nextRequestDate;
}
