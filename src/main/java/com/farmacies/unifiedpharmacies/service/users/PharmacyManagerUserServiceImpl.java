package com.farmacies.unifiedpharmacies.service.users;

import com.farmacies.unifiedpharmacies.exception.validation.BusinessValidationException;
import com.farmacies.unifiedpharmacies.model.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class PharmacyManagerUserServiceImpl implements UserRoleService {
    @Override
    public void validate(UserEntity user) {
        if (user.getPharmacy() == null) {
            throw new BusinessValidationException(
                    "PHARMACY_MANAGER must be linked to a pharmacy."
            );
        }


        if (user.getCrfRegistration() != null) {
            throw new BusinessValidationException(
                    "PHARMACY_MANAGER cannot have a CRF registration."
            );
        }
    }
}
