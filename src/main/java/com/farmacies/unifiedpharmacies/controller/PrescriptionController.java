package com.farmacies.unifiedpharmacies.controller;

import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.service.prescriptions.PrescriptionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {

    private final PrescriptionServiceImpl prescriptionServiceImpl;

    public PrescriptionController(
            PrescriptionServiceImpl prescriptionServiceImpl) {

        this.prescriptionServiceImpl = prescriptionServiceImpl;
    }


    @Operation(
            summary = "Find Prescription by ID",
            description = "Returns a Prescription by its ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> findPrescriptionById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                prescriptionServiceImpl.findPrescriptionById(id)
        );
    }


    @Operation(
            summary = "Find all Prescriptions",
            description = "Returns all registered Prescriptions."
    )
    @GetMapping
    public ResponseEntity<List<PrescriptionResponseDTO>> findAllPrescriptions() {
        return ResponseEntity.ok(
                prescriptionServiceImpl.findAllPrescriptions()
        );
    }


    @Operation(
            summary = "Update Prescription",
            description = "Updates the information of an existing Prescription."
    )
    @PatchMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> updatePrescription(
            @PathVariable Integer id,
            @Valid @RequestBody PrescriptionUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(
                prescriptionServiceImpl.updatePrescription(
                        id,
                        requestDTO
                )
        );
    }
}