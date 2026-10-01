package com.farmacies.unifiedpharmacies.medications;

import com.farmacies.unifiedpharmacies.dto.medications.MedicationCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medications.MedicationUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.MedicationEntity;
import com.farmacies.unifiedpharmacies.repository.MedicationRepository;
import com.farmacies.unifiedpharmacies.service.medications.MedicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MedicationServiceTest {

    private MedicationRepository medicationRepository;
    private MedicationServiceImpl medicationService;

    @BeforeEach
    void setup() {

        medicationRepository =
                Mockito.mock(MedicationRepository.class);

        medicationService =
                new MedicationServiceImpl(medicationRepository);
    }

    @Test
    void createMedication_success() {

        MedicationCreateRequestDTO request =
                new MedicationCreateRequestDTO();

        request.setName("Metformin");
        request.setDosage("500mg");
        request.setIndication("Diabetes");

        Mockito.when(
                medicationRepository.findByNameAndDosage(
                        "Metformin",
                        "500mg"
                )
        ).thenReturn(Optional.empty());

        MedicationEntity savedMedication =
                new MedicationEntity();

        savedMedication.setId(1);
        savedMedication.setName("Metformin");
        savedMedication.setDosage("500mg");
        savedMedication.setIndication("Diabetes");

        Mockito.when(
                medicationRepository.save(
                        Mockito.any(MedicationEntity.class)
                )
        ).thenReturn(savedMedication);

        MedicationResponseDTO response =
                medicationService.createMedication(request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Metformin", response.getName());
        assertEquals("500mg", response.getDosage());
        assertEquals("Diabetes", response.getIndication());

        Mockito.verify(medicationRepository)
                .findByNameAndDosage("Metformin", "500mg");

        Mockito.verify(medicationRepository)
                .save(Mockito.any(MedicationEntity.class));
    }

    @Test
    void createMedication_alreadyExists() {

        MedicationCreateRequestDTO request =
                new MedicationCreateRequestDTO();

        request.setName("Metformin");
        request.setDosage("500mg");
        request.setIndication("Diabetes");

        MedicationEntity existingMedication =
                new MedicationEntity();

        existingMedication.setId(1);
        existingMedication.setName("Metformin");
        existingMedication.setDosage("500mg");

        Mockito.when(
                medicationRepository.findByNameAndDosage(
                        "Metformin",
                        "500mg"
                )
        ).thenReturn(Optional.of(existingMedication));

        try {

            medicationService.createMedication(request);
            fail();

        } catch (ResourceConflictException e) {

            assertEquals(
                    "A medication with this name and dosage already exists.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationRepository)
                .findByNameAndDosage("Metformin", "500mg");

        Mockito.verify(
                medicationRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationEntity.class));
    }

    @Test
    void findMedicationById_found() {

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);
        medication.setName("Metformin");
        medication.setDosage("500mg");
        medication.setIndication("Diabetes");

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(medication));

        MedicationResponseDTO response =
                medicationService.findMedicationById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Metformin", response.getName());
        assertEquals("500mg", response.getDosage());
        assertEquals("Diabetes", response.getIndication());

        Mockito.verify(medicationRepository)
                .findById(1);
    }

    @Test
    void findMedicationById_notFound() {

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationService.findMedicationById(1);
            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationRepository)
                .findById(1);
    }

    @Test
    void findAllMedications_success() {

        MedicationEntity medication1 =
                new MedicationEntity();

        medication1.setId(1);
        medication1.setName("Metformin");
        medication1.setDosage("500mg");
        medication1.setIndication("Diabetes");

        MedicationEntity medication2 =
                new MedicationEntity();

        medication2.setId(2);
        medication2.setName("Glibenclamide");
        medication2.setDosage("5mg");
        medication2.setIndication("Diabetes");

        Mockito.when(medicationRepository.findAll())
                .thenReturn(List.of(
                        medication1,
                        medication2
                ));

        List<MedicationResponseDTO> response =
                medicationService.findAllMedications();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(
                "Metformin",
                response.get(0).getName()
        );

        assertEquals(
                "Glibenclamide",
                response.get(1).getName()
        );

        Mockito.verify(medicationRepository)
                .findAll();
    }

    @Test
    void updateMedication_success() {

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);
        medication.setName("Metformin");
        medication.setDosage("500mg");
        medication.setIndication("Diabetes");

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(medication));

        MedicationUpdateRequestDTO request =
                new MedicationUpdateRequestDTO();

        request.setName("Metformin");
        request.setDosage("850mg");
        request.setIndication("Diabetes");

        Mockito.when(
                medicationRepository.findByNameAndDosage(
                        "Metformin",
                        "850mg"
                )
        ).thenReturn(Optional.empty());

        MedicationEntity updatedMedication =
                new MedicationEntity();

        updatedMedication.setId(1);
        updatedMedication.setName("Metformin");
        updatedMedication.setDosage("850mg");
        updatedMedication.setIndication("Diabetes");

        Mockito.when(
                medicationRepository.save(
                        Mockito.any(MedicationEntity.class)
                )
        ).thenReturn(updatedMedication);

        MedicationResponseDTO response =
                medicationService.updateMedication(
                        1,
                        request
                );

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Metformin", response.getName());
        assertEquals("850mg", response.getDosage());
        assertEquals("Diabetes", response.getIndication());

        Mockito.verify(medicationRepository)
                .findById(1);

        Mockito.verify(medicationRepository)
                .findByNameAndDosage(
                        "Metformin",
                        "850mg"
                );

        Mockito.verify(medicationRepository)
                .save(Mockito.any(MedicationEntity.class));
    }

    @Test
    void updateMedication_notFound() {

        MedicationUpdateRequestDTO request =
                new MedicationUpdateRequestDTO();

        request.setName("Metformin");
        request.setDosage("850mg");
        request.setIndication("Diabetes");

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationService.updateMedication(
                    1,
                    request
            );

            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationRepository)
                .findById(1);

        Mockito.verify(
                medicationRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationEntity.class));
    }

    @Test
    void updateMedication_alreadyExists() {

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);
        medication.setName("Metformin");
        medication.setDosage("500mg");
        medication.setIndication("Diabetes");

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(medication));

        MedicationUpdateRequestDTO request =
                new MedicationUpdateRequestDTO();

        request.setName("Glibenclamide");
        request.setDosage("5mg");
        request.setIndication("Diabetes");

        MedicationEntity existingMedication =
                new MedicationEntity();

        existingMedication.setId(2);
        existingMedication.setName("Glibenclamide");
        existingMedication.setDosage("5mg");

        Mockito.when(
                medicationRepository.findByNameAndDosage(
                        "Glibenclamide",
                        "5mg"
                )
        ).thenReturn(Optional.of(existingMedication));

        try {

            medicationService.updateMedication(
                    1,
                    request
            );

            fail();

        } catch (ResourceConflictException e) {

            assertEquals(
                    "A medication with this name and dosage already exists.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationRepository)
                .findById(1);

        Mockito.verify(medicationRepository)
                .findByNameAndDosage(
                        "Glibenclamide",
                        "5mg"
                );

        Mockito.verify(
                medicationRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationEntity.class));
    }

    @Test
    void updateMedication_sameMedication() {

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);
        medication.setName("Metformin");
        medication.setDosage("500mg");
        medication.setIndication("Diabetes");

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(medication));

        MedicationUpdateRequestDTO request =
                new MedicationUpdateRequestDTO();

        request.setName("Metformin");
        request.setDosage("500mg");
        request.setIndication("Diabetes");

        Mockito.when(
                medicationRepository.findByNameAndDosage(
                        "Metformin",
                        "500mg"
                )
        ).thenReturn(Optional.of(medication));

        Mockito.when(
                medicationRepository.save(
                        Mockito.any(MedicationEntity.class)
                )
        ).thenReturn(medication);

        MedicationResponseDTO response =
                medicationService.updateMedication(
                        1,
                        request
                );

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Metformin", response.getName());
        assertEquals("500mg", response.getDosage());
        assertEquals("Diabetes", response.getIndication());

        Mockito.verify(medicationRepository)
                .findById(1);

        Mockito.verify(medicationRepository)
                .findByNameAndDosage(
                        "Metformin",
                        "500mg"
                );

        Mockito.verify(medicationRepository)
                .save(Mockito.any(MedicationEntity.class));
    }
}
