package com.farmacies.unifiedpharmacies.dto.users;

import com.farmacies.unifiedpharmacies.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Integer id;
    private String name;
    private String email;
    private String crfRegistration;
    private UserRole role;
}
