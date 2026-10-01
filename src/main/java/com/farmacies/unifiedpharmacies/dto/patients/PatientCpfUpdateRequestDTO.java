package com.farmacies.unifiedpharmacies.dto.patients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCpfUpdateRequestDTO {

    @NotBlank(message = "The CPF is required.")
    @Size(
            max = 14,
            message = "The CPF must contain at most 14 characters."
    )
    private String cpf;
}
