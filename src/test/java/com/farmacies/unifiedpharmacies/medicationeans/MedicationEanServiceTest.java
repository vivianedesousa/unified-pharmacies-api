package com.farmacies.unifiedpharmacies.medicationeans;

import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanResponseDTO;
import com.farmacies.unifiedpharmacies.dto.medicationeans.MedicationEanUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.MedicationEanEntity;
import com.farmacies.unifiedpharmacies.model.MedicationEntity;
import com.farmacies.unifiedpharmacies.repository.MedicationEanRepository;
import com.farmacies.unifiedpharmacies.repository.MedicationRepository;
import com.farmacies.unifiedpharmacies.service.medicationeans.MedicationEanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MedicationEanServiceTest {

    private MedicationEanRepository medicationEanRepository;
    private MedicationRepository medicationRepository;
    private MedicationEanServiceImpl medicationEanService;

    @BeforeEach
    void setup() {

        medicationEanRepository =
                Mockito.mock(MedicationEanRepository.class);

        medicationRepository =
                Mockito.mock(MedicationRepository.class);

        medicationEanService =
                new MedicationEanServiceImpl(
                        medicationEanRepository,
                        medicationRepository
                );
    }

    @Test
    void createMedicationEan_success() {

        MedicationEanCreateRequestDTO request =
                new MedicationEanCreateRequestDTO();

        request.setMedicationId(1);
        request.setEan("7891234567890");

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(medication));

        Mockito.when(
                medicationEanRepository.findByEan(
                        "7891234567890"
                )
        ).thenReturn(Optional.empty());

        MedicationEanEntity savedEan =
                new MedicationEanEntity();

        savedEan.setId(1);
        savedEan.setMedication(medication);
        savedEan.setEan("7891234567890");

        Mockito.when(
                medicationEanRepository.save(
                        Mockito.any(MedicationEanEntity.class)
                )
        ).thenReturn(savedEan);

        MedicationEanResponseDTO response =
                medicationEanService.createMedicationEan(request);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getMedicationId());
        assertEquals("7891234567890", response.getEan());

        Mockito.verify(medicationRepository)
                .findById(1);

        Mockito.verify(medicationEanRepository)
                .findByEan("7891234567890");

        Mockito.verify(medicationEanRepository)
                .save(Mockito.any(MedicationEanEntity.class));
    }

    @Test
    void createMedicationEan_medicationNotFound() {

        MedicationEanCreateRequestDTO request =
                new MedicationEanCreateRequestDTO();

        request.setMedicationId(1);
        request.setEan("7891234567890");

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationEanService.createMedicationEan(request);
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
                medicationEanRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationEanEntity.class));
    }

    @Test
    void createMedicationEan_alreadyExists() {

        MedicationEanCreateRequestDTO request =
                new MedicationEanCreateRequestDTO();

        request.setMedicationId(1);
        request.setEan("7891234567890");

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);

        Mockito.when(medicationRepository.findById(1))
                .thenReturn(Optional.of(medication));

        MedicationEanEntity existingEan =
                new MedicationEanEntity();

        existingEan.setId(2);
        existingEan.setMedication(medication);
        existingEan.setEan("7891234567890");

        Mockito.when(
                medicationEanRepository.findByEan(
                        "7891234567890"
                )
        ).thenReturn(Optional.of(existingEan));

        try {

            medicationEanService.createMedicationEan(request);
            fail();

        } catch (ResourceConflictException e) {

            assertEquals(
                    "This EAN is already registered.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationRepository)
                .findById(1);

        Mockito.verify(medicationEanRepository)
                .findByEan("7891234567890");

        Mockito.verify(
                medicationEanRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationEanEntity.class));
    }

    @Test
    void findMedicationEanById_found() {

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);

        MedicationEanEntity medicationEan =
                new MedicationEanEntity();

        medicationEan.setId(1);
        medicationEan.setMedication(medication);
        medicationEan.setEan("7891234567890");

        Mockito.when(medicationEanRepository.findById(1))
                .thenReturn(Optional.of(medicationEan));

        MedicationEanResponseDTO response =
                medicationEanService.findMedicationEanById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getMedicationId());
        assertEquals("7891234567890", response.getEan());

        Mockito.verify(medicationEanRepository)
                .findById(1);
    }

    @Test
    void findMedicationEanById_notFound() {

        Mockito.when(medicationEanRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationEanService.findMedicationEanById(1);
            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication EAN was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationEanRepository)
                .findById(1);
    }

    @Test
    void findAllMedicationEans_success() {

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);

        MedicationEanEntity ean1 =
                new MedicationEanEntity();

        ean1.setId(1);
        ean1.setMedication(medication);
        ean1.setEan("7891234567890");

        MedicationEanEntity ean2 =
                new MedicationEanEntity();

        ean2.setId(2);
        ean2.setMedication(medication);
        ean2.setEan("7899876543210");

        Mockito.when(medicationEanRepository.findAll())
                .thenReturn(List.of(ean1, ean2));

        List<MedicationEanResponseDTO> response =
                medicationEanService.findAllMedicationEans();

        assertNotNull(response);
        assertEquals(2, response.size());

        assertEquals(
                "7891234567890",
                response.get(0).getEan()
        );

        assertEquals(
                "7899876543210",
                response.get(1).getEan()
        );

        Mockito.verify(medicationEanRepository)
                .findAll();
    }

    @Test
    void updateMedicationEan_success() {

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);

        MedicationEanEntity medicationEan =
                new MedicationEanEntity();

        medicationEan.setId(1);
        medicationEan.setMedication(medication);
        medicationEan.setEan("7891234567890");

        Mockito.when(medicationEanRepository.findById(1))
                .thenReturn(Optional.of(medicationEan));

        MedicationEanUpdateRequestDTO request =
                new MedicationEanUpdateRequestDTO();

        request.setEan("7899999999999");

        Mockito.when(
                medicationEanRepository.findByEan(
                        "7899999999999"
                )
        ).thenReturn(Optional.empty());

        MedicationEanEntity updatedEan =
                new MedicationEanEntity();

        updatedEan.setId(1);
        updatedEan.setMedication(medication);
        updatedEan.setEan("7899999999999");

        Mockito.when(
                medicationEanRepository.save(
                        Mockito.any(MedicationEanEntity.class)
                )
        ).thenReturn(updatedEan);

        MedicationEanResponseDTO response =
                medicationEanService.updateMedicationEan(
                        1,
                        request
                );

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getMedicationId());
        assertEquals("7899999999999", response.getEan());

        Mockito.verify(medicationEanRepository)
                .findById(1);

        Mockito.verify(medicationEanRepository)
                .findByEan("7899999999999");

        Mockito.verify(medicationEanRepository)
                .save(Mockito.any(MedicationEanEntity.class));
    }

    @Test
    void updateMedicationEan_alreadyExists() {

        MedicationEntity medication =
                new MedicationEntity();

        medication.setId(1);

        MedicationEanEntity medicationEan =
                new MedicationEanEntity();

        medicationEan.setId(1);
        medicationEan.setMedication(medication);
        medicationEan.setEan("7891234567890");

        Mockito.when(medicationEanRepository.findById(1))
                .thenReturn(Optional.of(medicationEan));

        MedicationEanUpdateRequestDTO request =
                new MedicationEanUpdateRequestDTO();

        request.setEan("7899999999999");

        MedicationEanEntity existingEan =
                new MedicationEanEntity();

        existingEan.setId(2);
        existingEan.setMedication(medication);
        existingEan.setEan("7899999999999");

        Mockito.when(
                medicationEanRepository.findByEan(
                        "7899999999999"
                )
        ).thenReturn(Optional.of(existingEan));

        try {

            medicationEanService.updateMedicationEan(
                    1,
                    request
            );

            fail();

        } catch (ResourceConflictException e) {

            assertEquals(
                    "This EAN is already registered.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationEanRepository)
                .findById(1);

        Mockito.verify(medicationEanRepository)
                .findByEan("7899999999999");

        Mockito.verify(
                medicationEanRepository,
                Mockito.never()
        ).save(Mockito.any(MedicationEanEntity.class));
    }

    @Test
    void deleteMedicationEan_success() {

        MedicationEanEntity medicationEan =
                new MedicationEanEntity();

        medicationEan.setId(1);

        Mockito.when(medicationEanRepository.findById(1))
                .thenReturn(Optional.of(medicationEan));

        medicationEanService.deleteMedicationEan(1);

        Mockito.verify(medicationEanRepository)
                .findById(1);

        Mockito.verify(medicationEanRepository)
                .delete(medicationEan);
    }

    @Test
    void deleteMedicationEan_notFound() {

        Mockito.when(medicationEanRepository.findById(1))
                .thenReturn(Optional.empty());

        try {

            medicationEanService.deleteMedicationEan(1);
            fail();

        } catch (ResourceNotFoundException e) {

            assertEquals(
                    "Medication EAN was not found.",
                    e.getMessage()
            );
        }

        Mockito.verify(medicationEanRepository)
                .findById(1);

        Mockito.verify(
                medicationEanRepository,
                Mockito.never()
        ).delete(Mockito.any(MedicationEanEntity.class));
    }
}