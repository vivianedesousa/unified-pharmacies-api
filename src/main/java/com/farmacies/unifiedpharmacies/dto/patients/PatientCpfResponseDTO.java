package com.farmacies.unifiedpharmacies.dto.patients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCpfResponseDTO {
    private Integer id;
    private String fullName;
    private String cpf;
}