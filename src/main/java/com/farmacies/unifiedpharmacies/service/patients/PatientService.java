package com.farmacies.unifiedpharmacies.service.patients;

import com.farmacies.unifiedpharmacies.dto.patients.*;

import java.util.List;

public interface PatientService {

    PatientResponseDTO createPatient(
            PatientCreateRequestDTO requestDTO
    );

    PatientResponseDTO findPatientById(
            Integer id
    );

    List<PatientResponseDTO> findAllPatients();

    PatientResponseDTO updatePatient(
            Integer id,
            PatientUpdateRequestDTO requestDTO
    );

    PatientCpfResponseDTO updatePatientCpf(
            Integer id,
            PatientCpfUpdateRequestDTO requestDTO
    );

    PatientStatusResponseDTO findPatientStatus(Integer id);

    void deactivatePatient(Integer id);

    void reactivatePatient(Integer id);
}