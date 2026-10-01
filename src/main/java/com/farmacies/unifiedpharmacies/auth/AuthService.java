package com.farmacies.unifiedpharmacies.auth;

import com.farmacies.unifiedpharmacies.dto.auth.LoginRequestDTO;
import com.farmacies.unifiedpharmacies.dto.auth.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);
}
