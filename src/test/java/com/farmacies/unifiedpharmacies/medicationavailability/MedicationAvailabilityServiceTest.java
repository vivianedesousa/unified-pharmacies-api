package com.farmacies.unifiedpharmacies.medicationavailability;

import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityByEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationavailability.MedicationAvailabilityUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.MedicationAvailabilityEntity;
import com.farmacies.unifiedpharmacies.model.MedicationEanEntity;
import com.farmacies.unifiedpharmacies.model.MedicationEntity;
import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import com.farmacies.unifiedpharmacies.repository.MedicationAvailabilityRepository;
import com.farmacies.unifiedpharmacies.repository.MedicationEanRepository;
import com.farmacies.unifiedpharmacies.repository.MedicationRepository;
import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
import com.farmacies.unifiedpharmacies.service.medicationavailability.MedicationAvailabilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MedicationAvailabilityServiceTest {
    private MedicationAvailabilityRepository medicationAvailabilityRepository;
    private PharmacyRepository pharmacyRepository;
    private MedicationRepository medicationRepository;
    private MedicationEanRepository medicationEanRepository;
    private MedicationAvailabilityServiceImpl medicationAvailabilityService;

    @BeforeEach
    void setup() {

        medicationAvailabilityRepository =
                Mockito.mock(MedicationAvailabilityRepository.class);

        pharmacyRepository =
                Mockito.mock(PharmacyRepository.class);

        medicationRepository =
                Mockito.mock(MedicationRepository.class);

        medicationEanRepository =
                Mockito.mock(MedicationEanRepository.class);

        medicationAvailabilityService =
                new MedicationAvailabilityServiceImpl(
                        medicationAvailabilityRepository,
                        medicationAvailabilityRepository,
                        pharmacyRepository,
                        pharmacyRepository,
                        medicationRepository,
                        medicationEanRepository
                );
    }

    @Test
    void createMedicationAvailability_success() {

        MedicationAvailabilityCreateRequestDTO request =
                new MedicationAvailabilityCreateRequestDTO();

        request.setPharmacyId(1);
        request.setMedicationId(1);
        request.setQuantityInformed(10);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        MedicationEntity medication = new MedicationEntity();
        medication.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(medication));

        Mockito.when(
                medicationAvailabilityRepository
                        .findByPharmacyIdAndMedicationId(1, 1)
        ).thenReturn(Optional.empty());

        MedicationAvailabilityEntity saved =
                new MedicationAvailabilityEntity();

        saved.setId(1);
        saved.setPharmacy(pharmacy);
        saved.setMedication(medication);
        saved.setQuantityInformed(10);

        Mockito.when(
                medicationAvailabilityRepository.save(
                        Mockito.any(MedicationAvailabilityEntity.class)
                )
        ).thenReturn(saved);

        MedicationAvailabilityResponseDTO response =
                medicationAvailabilityService
                        .createMedicationAvailability(request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getPharmacyId());
        assertEquals(1, response.getMedicationId());
        assertEquals(10, response.getQuantityInformed());

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(medicationRepository)
                .findById(1);

        Mockito.verify(medicationAvailabilityRepository)
                .findByPharmacyIdAndMedicationId(1, 1);

        Mockito.verify(medicationAvailabilityRepository)
                .save(Mockito.any(MedicationAvailabilityEntity.class));
    }

    @Test
    void createMedicationAvailability_pharmacyNotFound() {

        MedicationAvailabilityCreateRequestDTO request =
                new MedicationAvailabilityCreateRequestDTO();

        request.setPharmacyId(1);
        request.setMedicationId(1);
        request.setQuantityInformed(10);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationAvailabilityService
                    .createMedicationAvailability(request);

            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Pharmacy was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(
                medicationAvailabilityRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationAvailabilityEntity.class));
    }

    @Test
    void createMedicationAvailability_medicationNotFound() {

        MedicationAvailabilityCreateRequestDTO request =
                new MedicationAvailabilityCreateRequestDTO();

        request.setPharmacyId(1);
        request.setMedicationId(1);
        request.setQuantityInformed(10);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationAvailabilityService
                    .createMedicationAvailability(request);

            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(pharmacyRepository)
                .findById(1);

        Mockito.verify(medicationRepository)
                .findById(1);

        Mockito.verify(
                medicationAvailabilityRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationAvailabilityEntity.class));
    }

    @Test
    void createMedicationAvailability_alreadyExists() {

        MedicationAvailabilityCreateRequestDTO request =
                new MedicationAvailabilityCreateRequestDTO();

        request.setPharmacyId(1);
        request.setMedicationId(1);
        request.setQuantityInformed(10);

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        MedicationEntity medication = new MedicationEntity();
        medication.setId(1);

        MedicationAvailabilityEntity existing =
                new MedicationAvailabilityEntity();

        existing.setId(2);
        existing.setPharmacy(pharmacy);
        existing.setMedication(medication);

        Mockito.when(pharmacyRepository.findById(1))
                .thenReturn(Optional.of(pharmacy));

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(medication));

        Mockito.when(
                medicationAvailabilityRepository
                        .findByPharmacyIdAndMedicationId(1, 1)
        ).thenReturn(Optional.of(existing));

        try {

            medicationAvailabilityService
                    .createMedicationAvailability(request);

            fail();

        } catch (ResourceConflictException e) {

            assertEquals(
                    "This pharmacy already has availability for this medication.",
                    e.getMessage()
            );
        }

        Mockito.verify(
                medicationAvailabilityRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationAvailabilityEntity.class));
    }

    @Test
    void findMedicationAvailabilityById_found() {

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        MedicationEntity medication = new MedicationEntity();
        medication.setId(1);

        MedicationAvailabilityEntity availability =
                new MedicationAvailabilityEntity();

        availability.setId(1);
        availability.setPharmacy(pharmacy);
        availability.setMedication(medication);
        availability.setQuantityInformed(10);

        Mockito.when(medicationAvailabilityRepository.findById(1))
                .thenReturn(Optional.of(availability));

        MedicationAvailabilityResponseDTO response =
                medicationAvailabilityService
                        .findMedicationAvailabilityById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getPharmacyId());
        assertEquals(1, response.getMedicationId());
        assertEquals(10, response.getQuantityInformed());

        Mockito.verify(medicationAvailabilityRepository)
                .findById(1);
    }

    @Test
    void findMedicationAvailabilityById_notFound() {

        Mockito.when(medicationAvailabilityRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationAvailabilityService
                    .findMedicationAvailabilityById(1);

            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication availability was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationAvailabilityRepository)
                .findById(1);
    }

    @Test
    void findAllMedicationAvailabilities_success() {

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        MedicationEntity medication = new MedicationEntity();
        medication.setId(1);

        MedicationAvailabilityEntity availability1 =
                new MedicationAvailabilityEntity();

        availability1.setId(1);
        availability1.setPharmacy(pharmacy);
        availability1.setMedication(medication);
        availability1.setQuantityInformed(10);

        MedicationAvailabilityEntity availability2 =
                new MedicationAvailabilityEntity();

        availability2.setId(2);
        availability2.setPharmacy(pharmacy);
        availability2.setMedication(medication);
        availability2.setQuantityInformed(5);

        Mockito.when(medicationAvailabilityRepository.findAll())
                .thenReturn(List.of(
                        availability1,
                        availability2
                ));

        List<MedicationAvailabilityResponseDTO> response =
                medicationAvailabilityService
                        .findAllMedicationAvailabilities();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(10, response.get(0).getQuantityInformed());
        assertEquals(5, response.get(1).getQuantityInformed());

        Mockito.verify(medicationAvailabilityRepository)
                .findAll();
    }

    @Test
    void updateMedicationAvailability_success() {

        PharmacyEntity pharmacy = new PharmacyEntity();
        pharmacy.setId(1);

        MedicationEntity medication = new MedicationEntity();
        medication.setId(1);

        MedicationAvailabilityEntity availability =
                new MedicationAvailabilityEntity();

        availability.setId(1);
        availability.setPharmacy(pharmacy);
        availability.setMedication(medication);
        availability.setQuantityInformed(10);

        Mockito.when(medicationAvailabilityRepository.findById(1))
                .thenReturn(Optional.of(availability));

        MedicationAvailabilityUpdateRequestDTO request =
                new MedicationAvailabilityUpdateRequestDTO();

        request.setQuantityInformed(20);

        Mockito.when(
                medicationAvailabilityRepository.save(
                        Mockito.any(MedicationAvailabilityEntity.class)
                )
        ).thenReturn(availability);

        MedicationAvailabilityResponseDTO response =
                medicationAvailabilityService
                        .updateMedicationAvailability(
                                1,
                                request
                        );

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(20, response.getQuantityInformed());

        Mockito.verify(medicationAvailabilityRepository)
                .findById(1);

        Mockito.verify(medicationAvailabilityRepository)
                .save(Mockito.any(MedicationAvailabilityEntity.class));
    }

    @Test
    void updateMedicationAvailability_notFound() {

        MedicationAvailabilityUpdateRequestDTO request =
                new MedicationAvailabilityUpdateRequestDTO();

        request.setQuantityInformed(20);

        Mockito.when(medicationAvailabilityRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationAvailabilityService
                    .updateMedicationAvailability(
                            1,
                            request
                    );

            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication availability was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationAvailabilityRepository)
                .findById(1);

        Mockito.verify(
                medicationAvailabilityRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationAvailabilityEntity.class));
    }

    @Test
    void findMedicationAvailabilityByEan_success() {

        MedicationEntity medication = new MedicationEntity();

        medication.setId(1);
        medication.setName("Metformin");
        medication.setDosage("500mg");

        MedicationEanEntity eanEntity =
                new MedicationEanEntity();

        eanEntity.setId(1);
        eanEntity.setMedication(medication);
        eanEntity.setEan("7891234567890");

        PharmacyEntity pharmacy =
                new PharmacyEntity();

        pharmacy.setId(1);
        pharmacy.setTradeName("Popular Pharmacy");
        pharmacy.setZipCode("29100-000");
        pharmacy.setState("ES");
        pharmacy.setCity("Serra");
        pharmacy.setNeighborhood("Centro");
        pharmacy.setStreet("Main Street");
        pharmacy.setNumber("100");
        pharmacy.setComplement("Store 1");

        MedicationAvailabilityEntity availability =
                new MedicationAvailabilityEntity();

        availability.setId(1);
        availability.setMedication(medication);
        availability.setPharmacy(pharmacy);
        availability.setQuantityInformed(10);

        Mockito.when(
                medicationEanRepository.findByEan(
                        "7891234567890"
                )
        ).thenReturn(Optional.of(eanEntity));

        Mockito.when(
                medicationAvailabilityRepository
                        .findAllByMedicationId(1)
        ).thenReturn(List.of(availability));

        List<MedicationAvailabilityByEanResponseDTO> response =
                medicationAvailabilityService
                        .findMedicationAvailabilityByEan(
                                "7891234567890"
                        );

        assertNotNull(response);
        assertEquals(1, response.size());

        Mockito.verify(medicationEanRepository)
                .findByEan("7891234567890");

        Mockito.verify(
                medicationAvailabilityRepository
        ).findAllByMedicationId(1);
    }

    @Test
    void findMedicationAvailabilityByEan_eanNotFound() {

        Mockito.when(
                medicationEanRepository.findByEan(
                        "7891234567890"
                )
        ).thenReturn(Optional.empty());

        try {

            medicationAvailabilityService
                    .findMedicationAvailabilityByEan(
                            "7891234567890"
                    );

            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication EAN was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationEanRepository)
                .findByEan("7891234567890");
    }

    @Test
    void deleteMedicationAvailability_success() {

        MedicationAvailabilityEntity availability =
                new MedicationAvailabilityEntity();

        availability.setId(1);

        Mockito.when(medicationAvailabilityRepository.findById(1))
                .thenReturn(Optional.of(availability));

        medicationAvailabilityService
                .deleteMedicationAvailability(1);

        Mockito.verify(medicationAvailabilityRepository)
                .findById(1);

        Mockito.verify(medicationAvailabilityRepository)
                .delete(availability);
    }

    @Test
    void deleteMedicationAvailability_notFound() {

        Mockito.when(medicationAvailabilityRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationAvailabilityService
                    .deleteMedicationAvailability(1);

            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication availability was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationAvailabilityRepository)
                .findById(1);

        Mockito.verify(
                medicationAvailabilityRepository,
                Mockito.never()
        ).delete(Mockito.any(MedicationAvailabilityEntity.class));
    }
}