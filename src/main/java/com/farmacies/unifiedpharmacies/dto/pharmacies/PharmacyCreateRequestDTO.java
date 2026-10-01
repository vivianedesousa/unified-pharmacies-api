package com.farmacies.unifiedpharmacies.dto.pharmacies;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyCreateRequestDTO {

    @NotBlank(message = "The CNPJ is required.")
    @Size(max = 18, message = "The CNPJ must contain at most 18 characters.")
    private String cnpj;

    @NotBlank(message = "The legal name is required.")
    @Size(max = 80, message = "The legal name must contain at most 80 characters.")
    private String legalName;

    @NotBlank(message = "The trade name is required.")
    @Size(max = 80, message = "The trade name must contain at most 80 characters.")
    private String tradeName;

    @NotBlank(message = "The email is required.")
    @Email(message = "The email must be valid.")
    @Size(max = 50, message = "The email must contain at most 50 characters.")
    private String email;

    @NotBlank(message = "The phone is required.")
    @Size(max = 20, message = "The phone must contain at most 20 characters.")
    private String phone;

    @NotBlank(message = "The ZIP code is required.")
    @Size(max = 9, message = "The ZIP code must contain at most 9 characters.")
    private String zipCode;

    @NotBlank(message = "The state is required.")
    @Size(max = 6, message = "The state must contain at most 6 characters.")
    private String state;

    @NotBlank(message = "The city is required.")
    @Size(max = 18, message = "The city must contain at most 18 characters.")
    private String city;

    @NotBlank(message = "The neighborhood is required.")
    @Size(max = 100, message = "The neighborhood must contain at most 100 characters.")
    private String neighborhood;

    @NotBlank(message = "The street is required.")
    @Size(max = 80, message = "The street must contain at most 80 characters.")
    private String street;

    @NotBlank(message = "The number is required.")
    @Size(max = 10, message = "The number must contain at most 10 characters.")
    private String number;

    @Size(max = 100, message = "The complement must contain at most 100 characters.")
    private String complement;
}
