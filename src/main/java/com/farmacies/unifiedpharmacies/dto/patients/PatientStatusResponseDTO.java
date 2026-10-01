package com.farmacies.unifiedpharmacies.dto.patients;

import com.farmacies.unifiedpharmacies.enums.PatientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientStatusResponseDTO {
    private Integer id;
    private PatientStatus status;
}
