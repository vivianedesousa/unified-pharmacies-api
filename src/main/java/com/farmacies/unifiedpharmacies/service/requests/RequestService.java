package com.farmacies.unifiedpharmacies.service.requests;

import com.farmacies.unifiedpharmacies.dto.requests.RequestAnalysisRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestResponseDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestUpdateRequestDTO;

import java.util.List;

public interface RequestService {

    RequestResponseDTO createRequest(
            RequestCreateRequestDTO requestDTO
    );

    RequestResponseDTO findRequestById(Integer id);

    List<RequestResponseDTO> findAllRequests();

    RequestResponseDTO updateRequest(
            Integer id,
            RequestUpdateRequestDTO requestDTO
    );

    RequestResponseDTO analyzeRequest(
            Integer requestId,
            RequestAnalysisRequestDTO requestDTO
    );
}