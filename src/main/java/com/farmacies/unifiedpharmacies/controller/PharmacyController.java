package com.farmacies.unifiedpharmacies.controller;

import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyResponseDTO;
import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.service.pharmacies.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import com.farmacies.unifiedpharmacies.service.pharmacies.PharmacyServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacies")
public class PharmacyController {

    private final PharmacyServiceImpl pharmacyServiceImpl;

    public PharmacyController(PharmacyService pharmacyService, PharmacyServiceImpl pharmacyServiceImpl) {
        this.pharmacyServiceImpl = pharmacyServiceImpl;
    }


    @Operation(
            summary = "Create pharmacy",
            description = "Endpoint responsible for creating a new pharmacy."
    )
    @PostMapping
    public ResponseEntity<PharmacyResponseDTO> createPharmacy(
            @Valid @RequestBody PharmacyCreateRequestDTO request) {
        PharmacyResponseDTO responseDTO =
                pharmacyServiceImpl.createPharmacy(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @Operation(
            summary = "Get pharmacy by ID",
            description = "Endpoint responsible for retrieving a pharmacy by ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<PharmacyResponseDTO> findPharmacyById(
            @PathVariable Integer id) {
        PharmacyResponseDTO responseDTO =
                pharmacyServiceImpl.findPharmacyById(id);
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "List all pharmacies",
            description = "Endpoint responsible for retrieving all pharmacies."
    )
    @GetMapping
    public ResponseEntity<List<PharmacyResponseDTO>> findAllPharmacies() {
        List<PharmacyResponseDTO> responseDTO =
                pharmacyServiceImpl.findAllPharmacies();
        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Get pharmacy by CNPJ",
            description = "Endpoint responsible for retrieving a pharmacy by CNPJ."
    )
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<PharmacyResponseDTO> findPharmacyByCnpj(
            @PathVariable String cnpj) {

        PharmacyResponseDTO responseDTO =
                pharmacyServiceImpl.findPharmacyByCnpj(cnpj);

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Update pharmacy",
            description = "Endpoint responsible for updating an existing pharmacy."
    )
    @PutMapping("/{id}")
    public ResponseEntity<PharmacyResponseDTO> updatePharmacy(
            @PathVariable Integer id,
            @Valid @RequestBody PharmacyUpdateRequestDTO request) {

        PharmacyResponseDTO responseDTO =
                pharmacyServiceImpl.updatePharmacy(id, request);

        return ResponseEntity.ok(responseDTO);
    }


    @Operation(
            summary = "Deactivate pharmacy",
            description = "Endpoint responsible for deactivating a pharmacy."
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivatePharmacy(
            @PathVariable Integer id) {
        pharmacyServiceImpl.deactivatePharmacy(id);
        return ResponseEntity.ok(
                "Pharmacy deactivated successfully."
        );
    }


    @Operation(
            summary = "Reactivate pharmacy",
            description = "Endpoint responsible for reactivating a pharmacy."
    )
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<PharmacyResponseDTO> reactivatePharmacy(
            @PathVariable Integer id) {
        PharmacyResponseDTO responseDTO =
                pharmacyServiceImpl.reactivatePharmacy(id);
        return ResponseEntity.ok(responseDTO);
    }
}
