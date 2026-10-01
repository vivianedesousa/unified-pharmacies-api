package com.farmacies.unifiedpharmacies.dto.pharmacies;

import com.farmacies.unifiedpharmacies.enums.PharmacyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyResponseDTO {

    private Integer id;

    private String cnpj;

    private String legalName;

    private String tradeName;

    private String email;

    private String phone;

    private String zipCode;

    private String state;

    private String city;

    private String neighborhood;

    private String street;

    private String number;

    private String complement;

    private PharmacyStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
