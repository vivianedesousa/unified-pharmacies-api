package com.farmacies.unifiedpharmacies.dto.users;

import com.farmacies.unifiedpharmacies.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    private String name;

    @Email(message = "The email must be valid.")
    private String email;

    @Size(
            min = 8,
            max = 64,
            message = "The password must contain between 8 and 64 characters."
    )
    private String password;

    private String crfRegistration;
    @NotNull(message = "The role is required.")
    private UserRole role;

    private Integer pharmacyId;
}
