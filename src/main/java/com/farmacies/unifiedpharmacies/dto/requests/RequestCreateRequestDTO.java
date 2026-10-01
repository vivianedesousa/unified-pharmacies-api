package com.farmacies.unifiedpharmacies.dto.requests;

import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionRequestDTO;//novo
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateRequestDTO {

    @NotNull(message = "The patient is required.")
    private Integer patientId;

    @NotNull(message = "The pharmacy is required.")
    private Integer pharmacyId;

    private String comment;

    @NotNull(message = "The prescription is required.")
    @Valid
    private PrescriptionRequestDTO prescription;
}
