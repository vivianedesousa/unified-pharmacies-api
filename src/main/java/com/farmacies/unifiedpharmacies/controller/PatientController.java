package com.farmacies.unifiedpharmacies.controller;

import com.farmacies.unifiedpharmacies.dto.patients.*;
import com.farmacies.unifiedpharmacies.service.patients.PatientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientServiceImpl patientServiceImpl;

    public PatientController(PatientServiceImpl patientServiceImpl) {
        this.patientServiceImpl = patientServiceImpl;
    }


    @Operation(
            summary = "Create patient",
            description = "Endpoint responsible for creating a new patient."
    )

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Valid @RequestBody PatientCreateRequestDTO requestDTO) {

        PatientResponseDTO responseDTO =
                patientServiceImpl.createPatient(requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }


    @Operation(
            summary = "Get patient by ID",
            description = "Endpoint responsible for retrieving a patient by ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> findPatientById(
            @PathVariable Integer id) {

        PatientResponseDTO responseDTO =
                patientServiceImpl.findPatientById(id);

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "List all patients",
            description = "Endpoint responsible for retrieving all patients."
    )
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> findAllPatients() {

        List<PatientResponseDTO> responseDTO =
                patientServiceImpl.findAllPatients();

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Update patient",
            description = "Endpoint responsible for updating patient data."
    )

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Integer id,
            @Valid @RequestBody PatientUpdateRequestDTO requestDTO) {

        PatientResponseDTO responseDTO =
                patientServiceImpl.updatePatient(
                        id,
                        requestDTO
                );
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Update patient CPF",
            description = "Endpoint responsible for updating only the CPF of a patient."
    )

    @PatchMapping("/{id}/cpf")
    public ResponseEntity<PatientCpfResponseDTO> updatePatientCpf(
            @PathVariable Integer id,
            @Valid @RequestBody PatientCpfUpdateRequestDTO requestDTO) {

        PatientCpfResponseDTO responseDTO =
                patientServiceImpl.updatePatientCpf(
                        id,
                        requestDTO);
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Find patient status",
            description = "Endpoint responsible for finding the current status of a patient."
    )
    @GetMapping("/{id}/status")
    public ResponseEntity<PatientStatusResponseDTO> findPatientStatus(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                patientServiceImpl.findPatientStatus(id)
        );
    }


    @Operation(
            summary = "Reactivate patient",
            description = "Endpoint responsible for reactivating a patient."
    )

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<String> reactivatePatient(
            @PathVariable Integer id) {

        patientServiceImpl.reactivatePatient(id);

        return ResponseEntity.ok(
                "Patient reactivated successfully."
        );
    }


    @Operation(
            summary = "Deactivate patient",
            description = "Endpoint responsible for deactivating a patient."
    )
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivatePatient(
            @PathVariable Integer id) {
        patientServiceImpl.deactivatePatient(id);
        return ResponseEntity.ok(
                "Patient deactivated successfully."
        );
    }
}

