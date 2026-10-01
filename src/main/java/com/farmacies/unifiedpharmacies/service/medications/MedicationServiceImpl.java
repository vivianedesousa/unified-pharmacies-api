package com.farmacies.unifiedpharmacies.service.medications;

import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.MedicationEntity;
import org.springframework.stereotype.Service;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.repository.MedicationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }


    @Override
    @Transactional
    public MedicationResponseDTO createMedication(MedicationCreateRequestDTO requestDTO) {


        Optional<MedicationEntity> medication =
                medicationRepository.findByNameAndDosage(
                        requestDTO.getName(),
                        requestDTO.getDosage()
                );

        if (medication.isPresent()) {
            throw new ResourceConflictException(
                    "A medication with this name and dosage already exists."
            );
        }

        MedicationEntity medicationEntity = new MedicationEntity();

        medicationEntity.setName(requestDTO.getName());
        medicationEntity.setDosage(requestDTO.getDosage());
        medicationEntity.setIndication(requestDTO.getIndication());

        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);

        medicationEntity.setCreatedAt(now);
        medicationEntity.setUpdatedAt(now);


        MedicationEntity saved = medicationRepository.save(medicationEntity);
        medicationRepository.save(medicationEntity);


        return new MedicationResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDosage(),
                saved.getIndication(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public MedicationResponseDTO findMedicationById(Integer id) {
        Optional<MedicationEntity> medication =
                medicationRepository.findById(id);

        if (medication.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication was not found."
            );
        }

        MedicationEntity saved = medication.get();

        return new MedicationResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDosage(),
                saved.getIndication(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }


    @Override
    @Transactional
    public List<MedicationResponseDTO> findAllMedications() {

        List<MedicationEntity> medications =
                medicationRepository.findAll();


        List<MedicationResponseDTO> responseMedications =
                new ArrayList<>();


        for (MedicationEntity medication : medications) {

            responseMedications.add(
                    new MedicationResponseDTO(
                            medication.getId(),
                            medication.getName(),
                            medication.getDosage(),
                            medication.getIndication(),
                            medication.getCreatedAt(),
                            medication.getUpdatedAt()
                    )
            );
        }

        return responseMedications;

    }

    @Override
    @Transactional
    public MedicationResponseDTO updateMedication(Integer id, MedicationUpdateRequestDTO requestDTO) {


        Optional<MedicationEntity> medication =
                medicationRepository.findById(id);


        if (medication.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication was not found."
            );
        }

        MedicationEntity saved = medication.get();


        Optional<MedicationEntity> existingMedication =
                medicationRepository.findByNameAndDosage(
                        requestDTO.getName(),
                        requestDTO.getDosage()
                );


        if (existingMedication.isPresent()
                && !existingMedication.get().getId().equals(id)) {
            throw new ResourceConflictException(
                    "A medication with this name and dosage already exists."
            );
        }


        saved.setName(requestDTO.getName());
        saved.setDosage(requestDTO.getDosage());
        saved.setIndication(requestDTO.getIndication());


        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);

        saved.setUpdatedAt(now);


        MedicationEntity updated = medicationRepository.save(saved);


        return new MedicationResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getDosage(),
                updated.getIndication(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
    }
}
