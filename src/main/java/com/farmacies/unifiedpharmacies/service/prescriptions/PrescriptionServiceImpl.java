package com.farmacies.unifiedpharmacies.service.prescriptions;

import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.PrescriptionEntity;
import com.farmacies.unifiedpharmacies.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionServiceImpl(
            PrescriptionRepository prescriptionRepository) {

        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponseDTO findPrescriptionById(Integer id) {


        Optional<PrescriptionEntity> prescription =
                prescriptionRepository.findById(id);


        if (prescription.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Prescription was not found."
            );
        }

        PrescriptionEntity saved = prescription.get();

        return new PrescriptionResponseDTO(
                saved.getId(),
                saved.getFilePath(),
                saved.getUsageInstructions(),
                saved.getPharmacistComment(),
                saved.getDurationDays(),
                saved.getContinuousUse(),
                saved.getNextRequestDate()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDTO> findAllPrescriptions() {


        List<PrescriptionEntity> prescriptions = prescriptionRepository.findAll();

        List<PrescriptionResponseDTO> responseList =
                new ArrayList<>();

        for (PrescriptionEntity prescription : prescriptions) {

            PrescriptionResponseDTO response =
                    new PrescriptionResponseDTO(
                            prescription.getId(),
                            prescription.getFilePath(),
                            prescription.getUsageInstructions(),
                            prescription.getPharmacistComment(),
                            prescription.getDurationDays(),
                            prescription.getContinuousUse(),
                            prescription.getNextRequestDate()
                    );

            responseList.add(response);
        }


        return responseList;
    }

    @Override
    @Transactional
    public PrescriptionResponseDTO updatePrescription(
            Integer id,
            PrescriptionUpdateRequestDTO requestDTO) {


        Optional<PrescriptionEntity> prescription =
                prescriptionRepository.findById(id);


        if (prescription.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Prescription was not found."
            );
        }

        PrescriptionEntity saved = prescription.get();

        if (requestDTO.getUsageInstructions() != null) {
            saved.setUsageInstructions(
                    requestDTO.getUsageInstructions()
            );
        }

        if (requestDTO.getPharmacistComment() != null) {
            saved.setPharmacistComment(
                    requestDTO.getPharmacistComment()
            );
        }

        if (requestDTO.getDurationDays() != null) {
            saved.setDurationDays(
                    requestDTO.getDurationDays()
            );
        }

        if (requestDTO.getContinuousUse() != null) {
            saved.setContinuousUse(
                    requestDTO.getContinuousUse()
            );
        }

        if (requestDTO.getNextRequestDate() != null) {
            saved.setNextRequestDate(
                    requestDTO.getNextRequestDate()
            );
        }

        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);

        saved.setUpdatedAt(now);


        PrescriptionEntity updated =   prescriptionRepository.save(saved);

        return new PrescriptionResponseDTO(
                updated.getId(),
                updated.getFilePath(),
                updated.getUsageInstructions(),
                updated.getPharmacistComment(),
                updated.getDurationDays(),
                updated.getContinuousUse(),
                updated.getNextRequestDate()
        );
    }
}

