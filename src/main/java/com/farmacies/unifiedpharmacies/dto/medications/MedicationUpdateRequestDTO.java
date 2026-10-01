package com.farmacies.unifiedpharmacies.dto.medications;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationUpdateRequestDTO {

    @NotBlank(message = "The medication name is required.")
    @Size(
            max = 60,
            message = "The medication name must contain at most 60 characters."
    )
    private String name;

    @NotBlank(message = "The dosage is required.")
    @Size(
            max = 10,
            message = "The dosage must contain at most 10 characters."
    )
    private String dosage;

    @NotBlank(message = "The indication is required.")
    @Size(
            max = 70,
            message = "The indication must contain at most 70 characters."
    )
    private String indication;
}
