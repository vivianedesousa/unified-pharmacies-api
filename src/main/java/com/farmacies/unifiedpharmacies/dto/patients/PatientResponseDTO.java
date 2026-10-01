package com.farmacies.unifiedpharmacies.dto.patients;

import com.farmacies.unifiedpharmacies.enums.PatientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {

    private Integer id;
    private String fullName;
    private String cpf;
    private String email;
    private String phone;
    private String zipCode;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String number;
    private String complement;
    private PatientStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}