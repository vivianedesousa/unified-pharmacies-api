package com.farmacies.unifiedpharmacies.dto.prescriptions;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDTO {

    private Integer id;
    private String filePath;
    private String usageInstructions;
    private String pharmacistComment;
    private Integer durationDays;
    private Boolean continuousUse;
    private LocalDate nextRequestDate;
}