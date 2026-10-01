package com.farmacies.unifiedpharmacies.controller;

import com.farmacies.unifiedpharmacies.dto.medications.MedicationCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.service.medications.MedicationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
public class MedicationController {

    private final MedicationServiceImpl medicationServiceImpl;

    public MedicationController(MedicationServiceImpl medicationServiceImpl) {
        this.medicationServiceImpl = medicationServiceImpl;
    }

    @Operation(
            summary = "Create medication",
            description = "Endpoint responsible for creating a new medication."
    )
    @PostMapping
    public ResponseEntity<MedicationResponseDTO> createMedication(
            @Valid @RequestBody MedicationCreateRequestDTO requestDTO) {
        MedicationResponseDTO responseDTO =
                medicationServiceImpl.createMedication(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @Operation(
            summary = "Get medication by ID",
            description = "Endpoint responsible for retrieving a medication by ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDTO> findMedicationById(
            @PathVariable Integer id) {
        MedicationResponseDTO responseDTO =
                medicationServiceImpl.findMedicationById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "List all medications",
            description = "Endpoint responsible for retrieving all medications."
    )

    @GetMapping
    public ResponseEntity<List<MedicationResponseDTO>> findAllMedications() {
        List<MedicationResponseDTO> responseDTO =
                medicationServiceImpl.findAllMedications();
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Update medication",
            description = "Endpoint responsible for updating an existing medication."
    )

    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponseDTO> updateMedication(
            @PathVariable Integer id,
            @Valid @RequestBody MedicationUpdateRequestDTO requestDTO) {
        MedicationResponseDTO responseDTO =
                medicationServiceImpl.updateMedication(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
