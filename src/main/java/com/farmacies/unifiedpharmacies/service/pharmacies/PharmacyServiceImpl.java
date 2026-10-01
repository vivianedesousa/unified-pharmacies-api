package com.farmacies.unifiedpharmacies.service.pharmacies;

import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyCreateRequestDTO;
import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyResponseDTO;
import com.farmacies.unifiedpharmacies.dto.pharmacies.PharmacyUpdateRequestDTO;
import com.farmacies.unifiedpharmacies.enums.PharmacyStatus;
import com.farmacies.unifiedpharmacies.exception.conflict.ResourceConflictException;
import com.farmacies.unifiedpharmacies.exception.notfound.ResourceNotFoundException;
import com.farmacies.unifiedpharmacies.model.PharmacyEntity;
import com.farmacies.unifiedpharmacies.repository.PharmacyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    public final PharmacyRepository pharmacyRepository;

    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    @Transactional
    public PharmacyResponseDTO createPharmacy(PharmacyCreateRequestDTO request) {
        Optional<PharmacyEntity> pharmacy =
                pharmacyRepository.findByCnpj(request.getCnpj());

        if (pharmacy.isPresent()) {
            throw new ResourceConflictException(
                    "A pharmacy with this CNPJ already exists."
            );
        }

        Optional<PharmacyEntity> existingEmail =
                pharmacyRepository.findByEmail(request.getEmail());

        if (existingEmail.isPresent()) {
            throw new ResourceConflictException(
                    "A pharmacy with this email already exists."
            );
        }

        PharmacyEntity pharmacyEntity = new PharmacyEntity();
        pharmacyEntity.setCnpj(request.getCnpj());
        pharmacyEntity.setLegalName(request.getLegalName());
        pharmacyEntity.setTradeName(request.getTradeName());
        pharmacyEntity.setEmail(request.getEmail());
        pharmacyEntity.setPhone(request.getPhone());
        pharmacyEntity.setZipCode(request.getZipCode());
        pharmacyEntity.setState(request.getState());
        pharmacyEntity.setCity(request.getCity());
        pharmacyEntity.setNeighborhood(request.getNeighborhood());
        pharmacyEntity.setStreet(request.getStreet());
        pharmacyEntity.setNumber(request.getNumber());
        pharmacyEntity.setComplement(request.getComplement());
        pharmacyEntity.setStatus(PharmacyStatus.ACTIVE);

        pharmacyEntity.setCreatedAt(LocalDateTime.now());
        pharmacyEntity.setUpdatedAt(LocalDateTime.now());

        LocalDateTime now =
                LocalDateTime.now().withSecond(0).withNano(0);

        pharmacyEntity.setCreatedAt(now);
        pharmacyEntity.setUpdatedAt(now);

        PharmacyEntity saved = pharmacyRepository.save(pharmacyEntity);

        return new PharmacyResponseDTO(
                saved.getId(),
                saved.getCnpj(),
                saved.getLegalName(),
                saved.getTradeName(),
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
    public PharmacyResponseDTO findPharmacyById(Integer id) {

        Optional<PharmacyEntity> pharmacy =
                pharmacyRepository.findById(id);

        if (pharmacy.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Pharmacy was not found."
            );
        }

        PharmacyEntity saved = pharmacy.get();

        return new PharmacyResponseDTO(
                saved.getId(),
                saved.getCnpj(),
                saved.getLegalName(),
                saved.getTradeName(),
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
    public List<PharmacyResponseDTO> findAllPharmacies() {


        List<PharmacyEntity> pharmacies = pharmacyRepository.findAll();


        List<PharmacyResponseDTO> responsePharmacies =
                new ArrayList<>();
        for (PharmacyEntity pharmacy : pharmacies) {
            responsePharmacies.add(
                    new PharmacyResponseDTO(
                            pharmacy.getId(),
                            pharmacy.getCnpj(),
                            pharmacy.getLegalName(),
                            pharmacy.getTradeName(),
                            pharmacy.getEmail(),
                            pharmacy.getPhone(),
                            pharmacy.getZipCode(),
                            pharmacy.getState(),
                            pharmacy.getCity(),
                            pharmacy.getNeighborhood(),
                            pharmacy.getStreet(),
                            pharmacy.getNumber(),
                            pharmacy.getComplement(),
                            pharmacy.getStatus(),
                            pharmacy.getCreatedAt(),
                            pharmacy.getUpdatedAt()

                    )
            );
        }

        return responsePharmacies;
    }

    @Override
    @Transactional
    public PharmacyResponseDTO updatePharmacy(
            Integer id,
            PharmacyUpdateRequestDTO request) {

        Optional<PharmacyEntity> pharmacy =
                pharmacyRepository.findById(id);


        if (pharmacy.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Pharmacy was not found."
            );
        }

        PharmacyEntity saved = pharmacy.get();

        Optional<PharmacyEntity> existingEmail =
                pharmacyRepository.findByEmail(request.getEmail());
        if (existingEmail.isPresent()
                && !existingEmail.get().getId().equals(id)) {
            throw new ResourceConflictException(
                    "A pharmacy with this email already exists."
            );
        }

        saved.setLegalName(request.getLegalName());
        saved.setTradeName(request.getTradeName());
        saved.setEmail(request.getEmail());
        saved.setPhone(request.getPhone());
        saved.setZipCode(request.getZipCode());
        saved.setState(request.getState());
        saved.setCity(request.getCity());
        saved.setNeighborhood(request.getNeighborhood());
        saved.setStreet(request.getStreet());
        saved.setNumber(request.getNumber());
        saved.setComplement(request.getComplement());

        saved.setUpdatedAt(LocalDateTime.now());
        PharmacyEntity updated = pharmacyRepository.save(saved);

        return new PharmacyResponseDTO(
                updated.getId(),
                updated.getCnpj(),
                updated.getLegalName(),
                updated.getTradeName(),
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
    @Transactional
    public PharmacyResponseDTO findPharmacyByCnpj(String cnpj) {
        Optional<PharmacyEntity> pharmacy = pharmacyRepository.findByCnpj(cnpj);


        if (pharmacy.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Pharmacy was not found."
            );
        }

        PharmacyEntity saved = pharmacy.get();

        return new PharmacyResponseDTO(
                saved.getId(),
                saved.getCnpj(),
                saved.getLegalName(),
                saved.getTradeName(),
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
    public void deactivatePharmacy(Integer id) {

        Optional<PharmacyEntity> pharmacy =
                pharmacyRepository.findById(id);


        if (pharmacy.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Pharmacy was not found."
            );
        }

        PharmacyEntity saved = pharmacy.get();

        if (saved.getStatus() == PharmacyStatus.INACTIVE) {
            throw new ResourceConflictException(
                    "Pharmacy is already inactive."
            );
        }


        saved.setStatus(PharmacyStatus.INACTIVE);


        saved.setUpdatedAt(LocalDateTime.now());


        pharmacyRepository.save(saved);
    }


    @Override
    @Transactional
    public PharmacyResponseDTO reactivatePharmacy(Integer id) {

        Optional<PharmacyEntity> pharmacy =
                pharmacyRepository.findById(id);

        if (pharmacy.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Pharmacy was not found."
            );
        }

        PharmacyEntity saved = pharmacy.get();

        if (saved.getStatus() == PharmacyStatus.ACTIVE) {
            throw new ResourceConflictException(
                    "Pharmacy is already active."
            );
        }


        saved.setStatus(PharmacyStatus.ACTIVE);

        saved.setUpdatedAt(LocalDateTime.now());


        PharmacyEntity updated =
                pharmacyRepository.save(saved);

        return new PharmacyResponseDTO(
                updated.getId(),
                updated.getCnpj(),
                updated.getLegalName(),
                updated.getTradeName(),
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
}

