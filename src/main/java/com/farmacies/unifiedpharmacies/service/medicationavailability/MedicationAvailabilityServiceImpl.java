package com.farmacies.unifiedpharmacies.service.medicationavailability;

import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.MedicationAvailabilityEntity;
import com.farmacies.unifiedpharmacies.model.MedicationEanEntity;
import com.farmacies.unifiedpharmacies.model.MedicationEntity;
import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import com.farmacies.unifiedpharmacies.repository.MedicationAvailabilityRepository;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.repository.MedicationEanRepository;
import com.farmacies.unifiedpharmacies.repository.MedicationRepository;
import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationAvailabilityServiceImpl implements MedicationAvailabilityService {
    private final MedicationAvailabilityRepository medicationAvailabilityRepository;
    private final PharmacyRepository pharmacyRepository;
    private final MedicationRepository medicationRepository;
    private final MedicationEanRepository medicationEanRepository;//// getmedicacao

    public MedicationAvailabilityServiceImpl(MedicationAvailabilityRepository medicationAvailability,
                                             MedicationAvailabilityRepository medicationAvailabilityRepository, com.farmacies.unifiedpharmacies.repository.PharmacyRepository pharmacyRepository, PharmacyRepository pharmacyRepository1, MedicationRepository medicationRepository, MedicationEanRepository medicationEanRepository) {
        this.medicationAvailabilityRepository = medicationAvailabilityRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.medicationRepository = medicationRepository;
        this.medicationEanRepository = medicationEanRepository;
    }

    @Override
    @Transactional
    public MedicationAvailabilityResponseDTO createMedicationAvailability(MedicationAvailabilityCreateRequestDTO requestDTO) {
        Optional<PharmacyEntity> pharmacy =
                pharmacyRepository.findById(
                        requestDTO.getPharmacyId()
                );

        if (pharmacy.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Pharmacy was not found."
            );
        }


        Optional<MedicationEntity> medication =
                medicationRepository.findById(
                        requestDTO.getMedicationId()
                );

        if (medication.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication was not found."
            );
        }


        Optional<MedicationAvailabilityEntity> existingAvailability =
                medicationAvailabilityRepository
                        .findByPharmacyIdAndMedicationId(
                                requestDTO.getPharmacyId(),
                                requestDTO.getMedicationId()
                        );

        if (existingAvailability.isPresent()) {
            throw new ResourceConflictException(
                    "This pharmacy already has availability for this medication."
            );
        }


        MedicationAvailabilityEntity availabilityEntity =
                new MedicationAvailabilityEntity();

        availabilityEntity.setPharmacy(pharmacy.get());
        availabilityEntity.setMedication(medication.get());
        availabilityEntity.setQuantityInformed(
                requestDTO.getQuantityInformed()
        );


        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);

        availabilityEntity.setCreatedAt(now);
        availabilityEntity.setUpdatedAt(now);

        // Passo 6: salva no banco
        MedicationAvailabilityEntity saved =
                medicationAvailabilityRepository.save(
                        availabilityEntity
                );


        return new MedicationAvailabilityResponseDTO(
                saved.getId(),
                saved.getPharmacy().getId(),
                saved.getMedication().getId(),
                saved.getQuantityInformed(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public MedicationAvailabilityResponseDTO findMedicationAvailabilityById(Integer id) {

        Optional<MedicationAvailabilityEntity> availability =
                medicationAvailabilityRepository.findById(id);

        // Passo 2: verifica se a Availability existe
        if (availability.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication availability was not found."
            );
        }

        MedicationAvailabilityEntity saved =
                availability.get();

        // Passo 3: retorna a Availability encontrada
        return new MedicationAvailabilityResponseDTO(
                saved.getId(),
                saved.getPharmacy().getId(),
                saved.getMedication().getId(),
                saved.getQuantityInformed(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public List<MedicationAvailabilityResponseDTO> findAllMedicationAvailabilities() {

        List<MedicationAvailabilityEntity> availabilities =
                medicationAvailabilityRepository.findAll();


        List<MedicationAvailabilityResponseDTO> responseAvailabilities =
                new ArrayList<>();


        for (MedicationAvailabilityEntity availability : availabilities) {

            responseAvailabilities.add(
                    new MedicationAvailabilityResponseDTO(
                            availability.getId(),
                            availability.getPharmacy().getId(),
                            availability.getMedication().getId(),
                            availability.getQuantityInformed(),
                            availability.getCreatedAt(),
                            availability.getUpdatedAt()
                    )
            );
        }
        return responseAvailabilities;
    }

    @Override
    @Transactional
    public MedicationAvailabilityResponseDTO updateMedicationAvailability(Integer id, MedicationAvailabilityUpdateRequestDTO requestDTO) {

        Optional<MedicationAvailabilityEntity> availability =
                medicationAvailabilityRepository.findById(id);

        // Passo 2: verifica se a Availability existe
        if (availability.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication availability was not found."
            );
        }

        MedicationAvailabilityEntity saved = availability.get();


        saved.setQuantityInformed(
                requestDTO.getQuantityInformed()
        );


        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);

        saved.setUpdatedAt(now);


        MedicationAvailabilityEntity updated =
                medicationAvailabilityRepository.save(saved);


        return new MedicationAvailabilityResponseDTO(
                updated.getId(),
                updated.getPharmacy().getId(),
                updated.getMedication().getId(),
                updated.getQuantityInformed(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
    }


    @Override
    @Transactional
    public List<MedicationAvailabilityByEanResponseDTO> findMedicationAvailabilityByEan(String ean) {

        Optional<MedicationEanEntity> medicationEan =
                medicationEanRepository.findByEan(ean);


        if (medicationEan.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication EAN was not found."
            );
        }
        MedicationEanEntity eanEntity = medicationEan.get();
        MedicationEntity medication = eanEntity.getMedication();

        List<MedicationAvailabilityEntity> availabilities =
                medicationAvailabilityRepository.findAllByMedicationId(
                        medication.getId()
                );


        List<MedicationAvailabilityByEanResponseDTO> response =
                new ArrayList<>();


        for (MedicationAvailabilityEntity availability : availabilities) {

            PharmacyEntity pharmacy = availability.getPharmacy();
            response.add(
                    new MedicationAvailabilityByEanResponseDTO(
                            medication.getId(),
                            medication.getName(),
                            medication.getDosage(),
                            eanEntity.getEan(),

                            pharmacy.getId(),
                            pharmacy.getTradeName(),
                            availability.getQuantityInformed(),

                            pharmacy.getZipCode(),
                            pharmacy.getState(),
                            pharmacy.getCity(),
                            pharmacy.getNeighborhood(),
                            pharmacy.getStreet(),
                            pharmacy.getNumber(),
                            pharmacy.getComplement()
                    )
            );
        }


        return response;
    }

    @Override
    @Transactional

    public void deleteMedicationAvailability(Integer id) {

        Optional<MedicationAvailabilityEntity> availability =
                medicationAvailabilityRepository.findById(id);


        if (availability.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Medication availability was not found."
            );
        }

        medicationAvailabilityRepository.delete(availability.get());
    }

}
