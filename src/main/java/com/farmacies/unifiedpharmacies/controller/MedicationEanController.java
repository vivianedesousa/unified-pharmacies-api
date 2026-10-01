package com.farmacies.unifiedpharmacies.controller;

import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.service.medicationeans.MedicationEanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medication-eans")
public class MedicationEanController {

    private final MedicationEanServiceImpl medicationEanServiceImpl;

    public MedicationEanController(MedicationEanServiceImpl medicationEanServiceImpl) {
        this.medicationEanServiceImpl = medicationEanServiceImpl;
    }

    @Operation(
            summary = "Create medication EAN",
            description = "Endpoint responsible for registering a new EAN code for a medication."
    )
    @PostMapping
    public ResponseEntity<MedicationEanResponseDTO> createMedicationEan(
            @Valid @RequestBody MedicationEanCreateRequestDTO requestDTO) {

        MedicationEanResponseDTO responseDTO =
                medicationEanServiceImpl.createMedicationEan(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @Operation(
            summary = "Get medication EAN by ID",
            description = "Endpoint responsible for retrieving a medication EAN by its ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<MedicationEanResponseDTO> findMedicationEanById(
            @PathVariable Integer id) {

        MedicationEanResponseDTO responseDTO =
                medicationEanServiceImpl.findMedicationEanById(id);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "List all medication EANs",
            description = "Endpoint responsible for retrieving all registered medication EANs."
    )
    @GetMapping
    public ResponseEntity<List<MedicationEanResponseDTO>> findAllMedicationEans() {

        List<MedicationEanResponseDTO> responseDTO =
                medicationEanServiceImpl.findAllMedicationEans();

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Update medication EAN",
            description = "Endpoint responsible for updating a medication EAN by its ID."
    )
    @PutMapping("/{id}")
    public ResponseEntity<MedicationEanResponseDTO> updateMedicationEan(
            @PathVariable Integer id,
            @Valid @RequestBody MedicationEanUpdateRequestDTO requestDTO) {

        MedicationEanResponseDTO responseDTO =
                medicationEanServiceImpl.updateMedicationEan(id, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Delete medication EAN",
            description = "Endpoint responsible for deleting a medication EAN by its ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicationEan(
            @PathVariable Integer id) {

        medicationEanServiceImpl.deleteMedicationEan(id);

        return ResponseEntity.ok(
                "Medication EAN deleted successfully."
        );
    }
}