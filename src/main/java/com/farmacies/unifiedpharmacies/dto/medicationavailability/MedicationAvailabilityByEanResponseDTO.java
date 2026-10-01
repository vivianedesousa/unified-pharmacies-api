
package com.farmacies.unifiedpharmacies.dto.medicationavailability;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationAvailabilityByEanResponseDTO {
    private Integer medicationId;
    private String medicationName;
    private String dosage;
    private String ean;
    private Integer pharmacyId;
    private String pharmacyName;
    private Integer quantityInformed;
    private String zipCode;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String number;
    private String complement;
}
