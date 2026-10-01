package com.farmacies.unifiedpharmacies.dto.prescriptions;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionUpdateRequestDTO {

    @Size(max = 255, message = "The usage instructions must contain at most 255 characters.")
    private String usageInstructions;

    @Size(max = 255, message = "The pharmacist comment must contain at most 255 characters.")
    private String pharmacistComment;

    @Positive(message = "The duration must be greater than zero.")
    private Integer durationDays;

    private Boolean continuousUse;

    private LocalDate nextRequestDate;
}
