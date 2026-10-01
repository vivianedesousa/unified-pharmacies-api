package com.farmacies.unifiedpharmacies.service.medicationeans;

import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.MedicationEanEntity;
import com.farmacies.unifiedpharmacies.model.MedicationEntity;
import com.farmacies.unifiedpharmacies.repository.MedicationEanRepository;
import com.farmacies.unifiedpharmacies.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationEanServiceImpl implements MedicationEanService {
    private final MedicationEanRepository medicationEanRepository;
    private final MedicationRepository medicationRepository;

    public MedicationEanServiceImpl(
            MedicationEanRepository medicationEanRepository,
            MedicationRepository medicationRepository) {

        this.medicationEanRepository = medicationEanRepository;
        this.medicationRepository = medicationRepository;
    }

    @Override
    @Transactional
    public MedicationEanResponseDTO createMedicationEan(MedicationEanCreateRequestDTO requestDTO) {

        Optional<MedicationEntity> medication =
                medicationRepository.findById(
                        requestDTO.getMedicationId()
                );

        if (medication.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication was not found."
            );
        }


        Optional<MedicationEanEntity> existingEan =
                medicationEanRepository.findByEan(
                        requestDTO.getEan()
                );

        if (existingEan.isPresent()) {
            throw new ResourceConflictException(
                    "This EAN is already registered."
            );
        }


        MedicationEanEntity medicationEanEntity =
                new MedicationEanEntity();

        medicationEanEntity.setMedication(medication.get());
        medicationEanEntity.setEan(requestDTO.getEan());


        MedicationEanEntity saved =
                medicationEanRepository.save(medicationEanEntity);


        return new MedicationEanResponseDTO(
                saved.getId(),
                saved.getMedication().getId(),
                saved.getEan()
        );

    }

    @Override
    @Transactional
    public MedicationEanResponseDTO findMedicationEanById(Integer id) {

        Optional<MedicationEanEntity> medicationEan = medicationEanRepository.findById(id);

        if (medicationEan.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication EAN was not found."
            );
        }

        MedicationEanEntity saved = medicationEan.get();


        return new MedicationEanResponseDTO(
                saved.getId(),
                saved.getMedication().getId(),
                saved.getEan()
        );
    }

    @Override
    @Transactional
    public List<MedicationEanResponseDTO> findAllMedicationEans() {

        List<MedicationEanEntity> medicationEans =
                medicationEanRepository.findAll();


        List<MedicationEanResponseDTO> responseMedicationEans =
                new ArrayList<>();


        for (MedicationEanEntity medicationEan : medicationEans) {

            responseMedicationEans.add(
                    new MedicationEanResponseDTO(
                            medicationEan.getId(),
                            medicationEan.getMedication().getId(),
                            medicationEan.getEan()
                    )
            );
        }

        return responseMedicationEans;
    }


    @Override
    public MedicationEanResponseDTO updateMedicationEan(Integer id, MedicationEanUpdateRequestDTO requestDTO) {


        Optional<MedicationEanEntity> medicationEan =
                medicationEanRepository.findById(id);


        if (medicationEan.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication EAN was not found."
            );
        }

        MedicationEanEntity saved = medicationEan.get();


        Optional<MedicationEanEntity> existingEan =
                medicationEanRepository.findByEan(
                        requestDTO.getEan()
                );

        if (existingEan.isPresent()
                && !existingEan.get().getId().equals(id)) {

            throw new ResourceConflictException(
                    "This EAN is already registered."
            );
        }


        saved.setEan(requestDTO.getEan());


        MedicationEanEntity updated =
                medicationEanRepository.save(saved);

        // Passo 6: retorna
        return new MedicationEanResponseDTO(
                updated.getId(),
                updated.getMedication().getId(),
                updated.getEan()
        );
    }

    @Override
    @Transactional
    public void deleteMedicationEan(Integer id) {

        Optional<MedicationEanEntity> medicationEan =
                medicationEanRepository.findById(id);


        if (medicationEan.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication EAN was not found."
            );
        }


        medicationEanRepository.delete(medicationEan.get());
    }
}
