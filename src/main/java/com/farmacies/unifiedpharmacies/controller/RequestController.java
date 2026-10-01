package com.farmacies.unifiedpharmacies.controller;

import com.farmacies.unifiedpharmacies.dto.requests.RequestUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestAnalysisRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestResponseDTO;
import com.farmacies.unifiedpharmacies.service.requests.RequestServiceImpl;
import com.farmacies.unifiedpharmacies.mailtrap.MailtrapService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {

    private final RequestServiceImpl requestServiceImpl;

    private final MailtrapService mailtrapService;

    public RequestController(RequestServiceImpl requestServiceImpl, MailtrapService mailtrapService) {
        this.requestServiceImpl = requestServiceImpl;
        this.mailtrapService = mailtrapService;
    }

    @Operation(
            summary = "Create request",
            description = "Endpoint responsible for creating a request with its prescription."
    )
    @PostMapping
    public ResponseEntity<RequestResponseDTO> createRequest(
            @Valid @RequestBody RequestCreateRequestDTO requestDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestServiceImpl.createRequest(requestDTO));
    }

    @Operation(
            summary = "Find request by ID",
            description = "Endpoint responsible for finding a request by its ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RequestResponseDTO> findRequestById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                requestServiceImpl.findRequestById(id)
        );
    }

    @Operation(
            summary = "Find all requests",
            description = "Endpoint responsible for finding all requests."
    )

    @GetMapping
    public ResponseEntity<List<RequestResponseDTO>> findAllRequests() {

        return ResponseEntity.ok(
                requestServiceImpl.findAllRequests()
        );
    }

    @Operation(
            summary = "Update request",
            description = "Endpoint responsible for updating a request comment."
    )

    @PutMapping("/{id}")
    public ResponseEntity<RequestResponseDTO> updateRequest(
            @PathVariable Integer id,
            @Valid @RequestBody RequestUpdateRequestDTO requestDTO) {

        return ResponseEntity.ok(
                requestServiceImpl.updateRequest(id, requestDTO)
        );
    }

    @Operation(
            summary = "Analyze medication availability",
            description = "Endpoint responsible for analyzing medication availability for a request using the EAN."
    )

    @PatchMapping("/{id}/medication-analysis")
    public ResponseEntity<RequestResponseDTO> analyzeRequest(
            @PathVariable Integer id,
            @Valid @RequestBody RequestAnalysisRequestDTO requestDTO) {

        return ResponseEntity.ok(
                requestServiceImpl.analyzeRequest(id, requestDTO)
        );
    }
}
