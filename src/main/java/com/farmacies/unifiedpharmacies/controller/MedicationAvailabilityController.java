package com.farmacies.unifiedpharmacies.controller;

import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.service.medicationavailability.MedicationAvailabilityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medication-availability")
public class MedicationAvailabilityController {
    private final MedicationAvailabilityServiceImpl medicationAvailabilityServiceImpl;

    public MedicationAvailabilityController(
            MedicationAvailabilityServiceImpl medicationAvailabilityServiceImpl) {
        this.medicationAvailabilityServiceImpl =
                medicationAvailabilityServiceImpl;
    }

    @Operation(
            summary = "Create medication availability",
            description = "Endpoint responsible for registering the availability of a medication in a pharmacy."
    )
    @PostMapping
    public ResponseEntity<MedicationAvailabilityResponseDTO>
    createMedicationAvailability(
            @Valid
            @RequestBody
            MedicationAvailabilityCreateRequestDTO requestDTO) {
        MedicationAvailabilityResponseDTO responseDTO =
                medicationAvailabilityServiceImpl
                        .createMedicationAvailability(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }


    @Operation(
            summary = "Get medication availability by ID",
            description = "Endpoint responsible for retrieving a medication availability record by ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<MedicationAvailabilityResponseDTO>
    findMedicationAvailabilityById(
            @PathVariable Integer id) {

        MedicationAvailabilityResponseDTO responseDTO =
                medicationAvailabilityServiceImpl
                        .findMedicationAvailabilityById(id);
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Update medication availability",
            description = "Endpoint responsible for updating the quantity informed for a medication in a pharmacy."
    )
    @PutMapping("/{id}")
    public ResponseEntity<MedicationAvailabilityResponseDTO>
    updateMedicationAvailability(
            @PathVariable Integer id,
            @Valid
            @RequestBody
            MedicationAvailabilityUpdateRequestDTO requestDTO) {
        MedicationAvailabilityResponseDTO responseDTO =
                medicationAvailabilityServiceImpl
                        .updateMedicationAvailability(
                                id,
                                requestDTO
                        );

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Delete medication availability",
            description = "Endpoint responsible for removing a medication availability record."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicationAvailability(
            @PathVariable Integer id) {

        medicationAvailabilityServiceImpl
                .deleteMedicationAvailability(id);

        return ResponseEntity.ok(
                "Medication availability deleted successfully."
        );
    }

    @Operation(summary = "Find medication availability by EAN",
            description = "Endpoint responsible for finding pharmacies where the medication is available using the EAN."
    )

    @GetMapping("/by-ean/{ean}")
    public ResponseEntity<List<MedicationAvailabilityByEanResponseDTO>>
    findMedicationAvailabilityByEan(
            @PathVariable String ean) {

        List<MedicationAvailabilityByEanResponseDTO> responseDTO =
                medicationAvailabilityServiceImpl
                        .findMedicationAvailabilityByEan(ean);

        return ResponseEntity.ok(responseDTO);
    }
}