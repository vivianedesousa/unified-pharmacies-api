package com.farmacies.unifiedpharmacies.service.patients;

import com.farmacies.unifiedpharmacies.dto.patients.*;
import com.farmacies.unifiedpharmacies.enums.PatientStatus;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.PatientEntity;
import com.farmacies.unifiedpharmacies.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public PatientResponseDTO createPatient(
            PatientCreateRequestDTO requestDTO) {
        Optional<PatientEntity> existingCpf =
                patientRepository.findByCpf(requestDTO.getCpf());

        if (existingCpf.isPresent()) {
            throw new ResourceConflictException(
                    "A patient with this CPF already exists."
            );
        }


        Optional<PatientEntity> existingEmail =
                patientRepository.findByEmail(requestDTO.getEmail());

        if (existingEmail.isPresent()) {
            throw new ResourceConflictException(
                    "A patient with this email already exists."
            );
        }


        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setFullName(requestDTO.getFullName());
        patientEntity.setCpf(requestDTO.getCpf());
        patientEntity.setEmail(requestDTO.getEmail());
        patientEntity.setPhone(requestDTO.getPhone());
        patientEntity.setPassword(requestDTO.getPassword());
        patientEntity.setZipCode(requestDTO.getZipCode());
        patientEntity.setState(requestDTO.getState());
        patientEntity.setCity(requestDTO.getCity());
        patientEntity.setNeighborhood(requestDTO.getNeighborhood());
        patientEntity.setStreet(requestDTO.getStreet());
        patientEntity.setNumber(requestDTO.getNumber());
        patientEntity.setComplement(requestDTO.getComplement());
        patientEntity.setStatus(PatientStatus.ACTIVE);

        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);

        patientEntity.setCreatedAt(now);
        patientEntity.setUpdatedAt(now);

        PatientEntity saved = patientRepository.save(patientEntity);

        return new PatientResponseDTO(
                saved.getId(),
                saved.getFullName(),
                saved.getCpf(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getZipCode(),
                saved.getState(),
                saved.getCity(),
                saved.getNeighborhood(),
                saved.getStreet(),
                saved.getNumber(),
                saved.getComplement(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public PatientResponseDTO findPatientById(Integer id) {


        Optional<PatientEntity> patient =
                patientRepository.findById(id);


        if (patient.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Patient was not found."
            );
        }

        PatientEntity saved = patient.get();


        return new PatientResponseDTO(
                saved.getId(),
                saved.getFullName(),
                saved.getCpf(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getZipCode(),
                saved.getState(),
                saved.getCity(),
                saved.getNeighborhood(),
                saved.getStreet(),
                saved.getNumber(),
                saved.getComplement(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public List<PatientResponseDTO> findAllPatients() {


        List<PatientEntity> patients =
                patientRepository.findAll();


        List<PatientResponseDTO> responsePatients =
                new ArrayList<>();


        for (PatientEntity patient : patients) {
            responsePatients.add(
                    new PatientResponseDTO(
                            patient.getId(),
                            patient.getFullName(),
                            patient.getCpf(),
                            patient.getEmail(),
                            patient.getPhone(),
                            patient.getZipCode(),
                            patient.getState(),
                            patient.getCity(),
                            patient.getNeighborhood(),
                            patient.getStreet(),
                            patient.getNumber(),
                            patient.getComplement(),
                            patient.getStatus(),
                            patient.getCreatedAt(),
                            patient.getUpdatedAt()
                    )
            );
        }

        return responsePatients;
    }


    @Override
    @Transactional
    public PatientResponseDTO updatePatient(
            Integer id,
            PatientUpdateRequestDTO requestDTO) {


        Optional<PatientEntity> patient =
                patientRepository.findById(id);

        if (patient.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Patient was not found."
            );
        }

        PatientEntity saved = patient.get();


        Optional<PatientEntity> existingEmail =
                patientRepository.findByEmail(
                        requestDTO.getEmail()
                );

        if (existingEmail.isPresent()
                && !existingEmail.get().getId().equals(id)) {

            throw new ResourceConflictException(
                    "A patient with this email already exists."
            );
        }

        saved.setFullName(requestDTO.getFullName());
        saved.setEmail(requestDTO.getEmail());
        saved.setPhone(requestDTO.getPhone());
        saved.setZipCode(requestDTO.getZipCode());
        saved.setState(requestDTO.getState());
        saved.setCity(requestDTO.getCity());
        saved.setNeighborhood(requestDTO.getNeighborhood());
        saved.setStreet(requestDTO.getStreet());
        saved.setNumber(requestDTO.getNumber());
        saved.setComplement(requestDTO.getComplement());

        if (requestDTO.getPassword() != null
                && !requestDTO.getPassword().isBlank()) {

            saved.setPassword(requestDTO.getPassword());
        }


        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);

        saved.setUpdatedAt(now);

        PatientEntity updated = patientRepository.save(saved);

        return new PatientResponseDTO(
                updated.getId(),
                updated.getFullName(),
                updated.getCpf(),
                updated.getEmail(),
                updated.getPhone(),
                updated.getZipCode(),
                updated.getState(),
                updated.getCity(),
                updated.getNeighborhood(),
                updated.getStreet(),
                updated.getNumber(),
                updated.getComplement(),
                updated.getStatus(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public PatientStatusResponseDTO findPatientStatus(Integer id) {

        Optional<PatientEntity> patient =
                patientRepository.findById(id);


        if (patient.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Patient was not found."
            );
        }

        PatientEntity saved = patient.get();

        return new PatientStatusResponseDTO(
                saved.getId(),
                saved.getStatus()
        );
    }


    @Override
    @Transactional
    public PatientCpfResponseDTO updatePatientCpf(
            Integer id,
            PatientCpfUpdateRequestDTO requestDTO) {

        Optional<PatientEntity> patient =
                patientRepository.findById(id);

        if (patient.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Patient was not found."
            );
        }

        PatientEntity saved = patient.get();

        Optional<PatientEntity> existingCpf =
                patientRepository.findByCpf(
                        requestDTO.getCpf()
                );

        if (existingCpf.isPresent()
                && !existingCpf.get().getId().equals(id)) {

            throw new ResourceConflictException(
                    "A patient with this CPF already exists."
            );
        }


        saved.setCpf(requestDTO.getCpf());

        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);
        saved.setUpdatedAt(now);

        PatientEntity updated = patientRepository.save(saved);

        return new PatientCpfResponseDTO(
                updated.getId(),
                updated.getFullName(),
                updated.getCpf()
        );
    }

    @Override
    @Transactional
    public void reactivatePatient(Integer id) {

        Optional<PatientEntity> patient = patientRepository.findById(id);


        if (patient.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Patient was not found."
            );
        }

        PatientEntity saved = patient.get();

        if (saved.getStatus() == PatientStatus.ACTIVE) {
            throw new ResourceConflictException(
                    "Patient is already active."
            );
        }


        saved.setStatus(PatientStatus.ACTIVE);

        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);
        saved.setUpdatedAt(now);

        patientRepository.save(saved);
    }


    @Override
    @Transactional
    public void deactivatePatient(Integer id) {

        Optional<PatientEntity> patient =
                patientRepository.findById(id);

        if (patient.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Patient was not found.");
        }

        PatientEntity saved = patient.get();

        if (saved.getStatus() == PatientStatus.INACTIVE) {
            throw new ResourceConflictException(
                    "Patient is already inactive."
            );
        }

        saved.setStatus(PatientStatus.INACTIVE);
        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);
        saved.setUpdatedAt(now);

        patientRepository.save(saved);
    }

}