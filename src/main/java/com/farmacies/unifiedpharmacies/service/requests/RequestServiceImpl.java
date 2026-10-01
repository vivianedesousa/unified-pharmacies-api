package com.farmacies.unifiedpharmacies.service.requests;
//import com.farmacies.unifiedpharmacies.mailtrap.MailtrapService;
//import com.farmacies.unifiedpharmacies.service.email.EmailService;
//import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestResponseDTO;
//import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
//import com.farmacies.unifiedpharmacies.enums.PharmacyStatus;
//import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
//import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestUpdateRequestDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestAnalysisRequestDTO;
//import com.farmacies.unifiedpharmacies.model.PatientEntity;
//import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
//import com.farmacies.unifiedpharmacies.model.PrescriptionEntity;
//import com.farmacies.unifiedpharmacies.model.RequestEntity;
//import com.farmacies.unifiedpharmacies.repository.PatientRepository;
//import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
//import com.farmacies.unifiedpharmacies.repository.PrescriptionRepository;
//import com.farmacies.unifiedpharmacies.repository.RequestRepository;
//import com.farmacies.unifiedpharmacies.enums.RequestStatus;
//import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
//import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionRequestDTO;
//import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestAnalysisRequestDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestCreateRequestDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestResponseDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestUpdateRequestDTO;
//
//import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
//import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionRequestDTO;
//import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestAnalysisRequestDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestCreateRequestDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestResponseDTO;
//import com.farmacies.unifiedpharmacies.dto.requests.RequestUpdateRequestDTO;
////import com.farmacies.unifiedpharmacies.dto.requests.PrescriptionRequestDTO
//
//import com.farmacies.unifiedpharmacies.service.email.EmailService;
//import com.farmacies.unifiedpharmacies.service.medicationavailability.MedicationAvailabilityService;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;

import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionRequestDTO;
import com.farmacies.unifiedpharmacies.dto.prescriptions.PrescriptionResponseDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestAnalysisRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestResponseDTO;
import com.farmacies.unifiedpharmacies.dto.requests.RequestUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.enums.PharmacyStatus;
import com.farmacies.unifiedpharmacies.enums.RequestStatus;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.mailtrap.MailtrapService;
import com.farmacies.unifiedpharmacies.model.PatientEntity;
import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import com.farmacies.unifiedpharmacies.model.PrescriptionEntity;
import com.farmacies.unifiedpharmacies.model.RequestEntity;
import com.farmacies.unifiedpharmacies.repository.PatientRepository;
import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
import com.farmacies.unifiedpharmacies.repository.PrescriptionRepository;
import com.farmacies.unifiedpharmacies.repository.RequestRepository;
import com.farmacies.unifiedpharmacies.service.email.EmailService;
import com.farmacies.unifiedpharmacies.service.medicationavailability.MedicationAvailabilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final PatientRepository patientRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicationAvailabilityService medicationAvailabilityService;
    private final EmailService emailService;

    public RequestServiceImpl(
            RequestRepository requestRepository,
            PatientRepository patientRepository,
            PharmacyRepository pharmacyRepository,
            PrescriptionRepository prescriptionRepository,
            MedicationAvailabilityService medicationAvailabilityService,
            MailtrapService mailtrapService, EmailService emailService) {

        this.requestRepository = requestRepository;
        this.patientRepository = patientRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.medicationAvailabilityService = medicationAvailabilityService;
        // novo
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public RequestResponseDTO createRequest(RequestCreateRequestDTO requestDTO) {

        Optional<PatientEntity> patient =
                patientRepository.findById(requestDTO.getPatientId());

        if (patient.isEmpty()) {
            throw new ResourceNotFoundException("Patient was not found.");
        }


        Optional<PharmacyEntity> pharmacy =
                pharmacyRepository.findById(requestDTO.getPharmacyId());

        if (pharmacy.isEmpty()) {
            throw new ResourceNotFoundException("Pharmacy was not found.");
        }


        RequestEntity request = new RequestEntity();

        request.setPatient(patient.get());
        request.setPharmacy(pharmacy.get());


        request.setSelectedPharmacy(null);


        request.setStatus(RequestStatus.SUBMITTED);

        request.setComment(requestDTO.getComment());

        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);

        request.setCreatedAt(now);
        request.setUpdatedAt(now);

        // Step 4 - save request
        RequestEntity savedRequest =
                requestRepository.save(request);


        PrescriptionRequestDTO prescriptionDTO =
                requestDTO.getPrescription();

        PrescriptionEntity prescription =
                new PrescriptionEntity();


        prescription.setRequest(savedRequest);

        prescription.setFilePath(
                prescriptionDTO.getFilePath()
        );

        prescription.setUsageInstructions(
                prescriptionDTO.getUsageInstructions()
        );

        prescription.setDurationDays(
                prescriptionDTO.getDurationDays()
        );

        prescription.setContinuousUse(
                prescriptionDTO.getContinuousUse()
        );

        prescription.setNextRequestDate(
                prescriptionDTO.getNextRequestDate()
        );

        prescription.setCreatedAt(now);
        prescription.setUpdatedAt(now);

        // Step 7 - find prescriptions linked to request
        List<PrescriptionEntity> prescriptions =
                prescriptionRepository.findAllByRequestId(
                        savedRequest.getId()
                );

        // Step 8 - return response
        return convertToResponseDTO(
                savedRequest,
                prescriptions
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RequestResponseDTO findRequestById(Integer id) {


        Optional<RequestEntity> request = requestRepository.findById(id);

        if (request.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Request was not found."
            );
        }

        RequestEntity savedRequest = request.get();

        List<PrescriptionEntity> prescriptions =
                prescriptionRepository.findAllByRequestId(id);

        return convertToResponseDTO(
                savedRequest,
                prescriptions
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestResponseDTO> findAllRequests() {

        List<RequestEntity> requests = requestRepository.findAll();


        List<RequestResponseDTO> responseList =
                new ArrayList<>();


        for (RequestEntity request : requests) {

            List<PrescriptionEntity> prescriptions =
                    prescriptionRepository.findAllByRequestId(
                            request.getId()
                    );

            responseList.add(
                    convertToResponseDTO(
                            request,
                            prescriptions
                    )
            );
        }

        return responseList;
    }


    @Override
    @Transactional
    public RequestResponseDTO updateRequest(
            Integer id,
            RequestUpdateRequestDTO requestDTO) {

        Optional<RequestEntity> request = requestRepository.findById(id);

        if (request.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Request was not found."
            );
        }

        RequestEntity savedRequest = request.get();

        savedRequest.setComment(
                requestDTO.getComment()
        );

        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);

        savedRequest.setUpdatedAt(now);


        RequestEntity updatedRequest = requestRepository.save(savedRequest);

        List<PrescriptionEntity> prescriptions =
                prescriptionRepository.findAllByRequestId(
                        updatedRequest.getId()
                );

        return convertToResponseDTO(
                updatedRequest,
                prescriptions
        );
    }

    private RequestResponseDTO convertToResponseDTO(
            RequestEntity request,
            List<PrescriptionEntity> prescriptions) {

        List<PrescriptionResponseDTO> prescriptionResponses =
                new ArrayList<>();

        for (PrescriptionEntity prescription : prescriptions) {

            prescriptionResponses.add(
                    new PrescriptionResponseDTO(
                            prescription.getId(),
                            prescription.getFilePath(),
                            prescription.getUsageInstructions(),
                            prescription.getPharmacistComment(),
                            prescription.getDurationDays(),
                            prescription.getContinuousUse(),
                            prescription.getNextRequestDate()
                    )
            );
        }

        return new RequestResponseDTO(
                request.getId(),
                request.getPatient().getId(),
                request.getPharmacy().getId(),
                request.getSelectedPharmacy() != null
                        ? request.getSelectedPharmacy().getId()
                        : null,
                request.getStatus(),
                request.getComment(),
                prescriptionResponses,
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public RequestResponseDTO analyzeRequest(
            Integer requestId,
            RequestAnalysisRequestDTO requestDTO) {

        Optional<RequestEntity> request =
                requestRepository.findById(requestId);

        if (request.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Request was not found."
            );
        }

        RequestEntity savedRequest = request.get();

        savedRequest.setStatus(RequestStatus.UNDER_REVIEW);


        List<MedicationAvailabilityByEanResponseDTO> availabilities =
                medicationAvailabilityService
                        .findMedicationAvailabilityByEan(
                                requestDTO.getEan()
                        );

        Integer initialPharmacyId =
                savedRequest.getPharmacy().getId();


        boolean availableAtInitialPharmacy = false;

        for (MedicationAvailabilityByEanResponseDTO availability
                : availabilities) {


            if (availability.getPharmacyId().equals(initialPharmacyId)
                    && availability.getQuantityInformed() > 0) {

                availableAtInitialPharmacy = true;
                break;
            }
        }


        if (availableAtInitialPharmacy) {

            savedRequest.setSelectedPharmacy(null);

            savedRequest.setStatus(
                    RequestStatus.AVAILABLE_AT_INITIAL_PHARMACY
            );

        } else {

            Integer selectedPharmacyId = requestDTO.getSelectedPharmacyId();


            boolean hasAlternativePharmacy = false;

            for (MedicationAvailabilityByEanResponseDTO availability
                    : availabilities) {

                if (!availability.getPharmacyId().equals(initialPharmacyId)
                        && availability.getQuantityInformed() > 0) {

                    hasAlternativePharmacy = true;
                    break;
                }
            }


            if (!hasAlternativePharmacy) {

                savedRequest.setSelectedPharmacy(null);

                savedRequest.setStatus(
                        RequestStatus.MEDICATION_NOT_FOUND
                );

            } else {


                if (selectedPharmacyId == null) {

                    throw new ResourceConflictException(
                            "An alternative pharmacy must be selected."
                    );
                }

                if (selectedPharmacyId.equals(initialPharmacyId)) {

                    throw new ResourceConflictException(
                            "The selected pharmacy must be different from the initial pharmacy."
                    );
                }


                boolean selectedPharmacyAvailable = false;

                for (MedicationAvailabilityByEanResponseDTO availability
                        : availabilities) {

                    if (availability.getPharmacyId()
                            .equals(selectedPharmacyId)
                            && availability.getQuantityInformed() > 0) {

                        selectedPharmacyAvailable = true;
                        break;
                    }
                }


                if (!selectedPharmacyAvailable) {

                    throw new ResourceConflictException(
                            "The selected pharmacy does not have the medication available."
                    );
                }

                // Step 12 - find the selected pharmacy
                Optional<PharmacyEntity> selectedPharmacy =
                        pharmacyRepository.findById(selectedPharmacyId);

                if (selectedPharmacy.isEmpty()) {

                    throw new ResourceNotFoundException(
                            "Selected pharmacy was not found."
                    );
                }

                if (selectedPharmacy.get().getStatus()
                        == PharmacyStatus.INACTIVE) {

                    throw new ResourceConflictException(
                            "The selected pharmacy is inactive."
                    );
                }

                savedRequest.setSelectedPharmacy(
                        selectedPharmacy.get()
                );


                savedRequest.setStatus(
                        RequestStatus.ALTERNATIVE_PHARMACY_SELECTED
                );
            }
        }


        LocalDateTime now =
                LocalDateTime.now()
                        .withSecond(0)
                        .withNano(0);

        savedRequest.setUpdatedAt(now);


        RequestEntity updatedRequest = requestRepository.save(savedRequest);


        List<PrescriptionEntity> prescriptions =
                prescriptionRepository.findAllByRequestId(
                        updatedRequest.getId()
                );


        String patientName = updatedRequest.getPatient().getFullName();

        String patientEmail = updatedRequest.getPatient().getEmail();

        Integer finalPharmacyId = null;

        if (updatedRequest.getStatus()
                == RequestStatus.AVAILABLE_AT_INITIAL_PHARMACY) {

            finalPharmacyId =
                    updatedRequest.getPharmacy().getId();

        } else if (updatedRequest.getStatus()
                == RequestStatus.ALTERNATIVE_PHARMACY_SELECTED) {

            finalPharmacyId =
                    updatedRequest.getSelectedPharmacy().getId();
        }

        MedicationAvailabilityByEanResponseDTO finalAvailability = null;

        if (finalPharmacyId != null) {

            for (MedicationAvailabilityByEanResponseDTO availability
                    : availabilities) {

                if (availability.getPharmacyId()
                        .equals(finalPharmacyId)) {

                    finalAvailability = availability;
                    break;
                }
            }
        }

        emailService.sendMedicationAnalysisEmail(
                patientName,
                patientEmail,
                updatedRequest.getStatus(),
                finalAvailability
        );

        return convertToResponseDTO(
                updatedRequest,
                prescriptions
        );
    }
}


