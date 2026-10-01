package com.farmacies.unifiedpharmacies.dto.requests;

import com.farmacies.unifiedpharmacies.enums.RequestStatus;
import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestResponseDTO {
    private Integer id;
    private Integer patientId;
    private Integer pharmacyId;
    private Integer selectedPharmacyId;
    private RequestStatus status;
    private String comment;
    // private PrescriptionResponseDTO prescription;
    private List<PrescriptionResponseDTO> prescriptions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    public RequestResponseDTO(Integer id, Integer id1, Integer id2,
//                              Integer selectedPharmacyId, RequestStatus status,
//                              String comment, List<com.farmacies.unifiedpharmacies.dto
//            .requests.PrescriptionResponseDTO> prescriptionResponses, LocalDateTime createdAt,
//                              LocalDateTime updatedAt) {
//    }
}