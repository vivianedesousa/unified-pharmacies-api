package com.farmacies.unifiedpharmacies.service.users;

import com.farmacies.unifiedpharmacies.model.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class SystemAdminUserServiceImpl implements UserRoleService {
    @Override
    public void validate(UserEntity user) {

        if (user.getPharmacy() != null) {
            throw new RuntimeException(
                    "SYSTEM_ADMIN cannot be linked to a pharmacy."
            );
        }

        if (user.getCrfRegistration() != null) {
            throw new RuntimeException(
                    "SYSTEM_ADMIN cannot have a CRF registration."
            );
        }
    }
}