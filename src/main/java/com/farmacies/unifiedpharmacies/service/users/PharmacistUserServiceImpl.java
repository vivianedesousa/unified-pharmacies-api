package com.farmacies.unifiedpharmacies.service.users;

import com.farmacies.unifiedpharmacies.exception.validation.BusinessValidationException;
import com.farmacies.unifiedpharmacies.model.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class PharmacistUserServiceImpl implements UserRoleService {
    @Override
    public void validate(UserEntity user) {
        if (user.getPharmacy() == null) {
            throw new BusinessValidationException(
                    "PHARMACIST must be linked to a pharmacy."
            );
        }

        if (user.getCrfRegistration() == null ||
                user.getCrfRegistration().isBlank()) {
            throw new BusinessValidationException(
                    "PHARMACIST must have a CRF registration."
            );
        }
    }
}

