package com.farmacies.unifiedpharmacies.auth;
import com.farmacies.unifiedpharmacies.dto.auth.LoginRequestDTO;
import com.farmacies.unifiedpharmacies.dto.auth.LoginResponseDTO;
import com.farmacies.unifiedpharmacies.model.PatientEntity;
import com.farmacies.unifiedpharmacies.model.UserEntity;
import com.farmacies.unifiedpharmacies.repository.PatientRepository;
import com.farmacies.unifiedpharmacies.repository.UserRepository;
import com.farmacies.unifiedpharmacies.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.farmacies.unifiedpharmacies.exception.validation.BusinessValidationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        UserEntity user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user != null) {

            if (!passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword()
            )) {
                throw new BusinessValidationException("Invalid email or password.");
            }

            String role = user.getRole().name();

            String token = jwtService.generateToken(
                    user.getEmail(),
                    role
            );

            return new LoginResponseDTO(
                    token,
                    user.getEmail(),
                    role
            );
        }

        PatientEntity patient = patientRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (patient != null) {

            if (!passwordEncoder.matches(
                    request.getPassword(),
                    patient.getPassword()
            )) {
                throw new BusinessValidationException("Invalid email or password.");
            }

            if (patient.getStatus().name().equals("INACTIVE")) {
                throw new BusinessValidationException("Patient is inactive.");
            }

            String role = "PATIENT";

            String token = jwtService.generateToken(
                    patient.getEmail(),
                    role
            );

            return new LoginResponseDTO(
                    token,
                    patient.getEmail(),
                    role
            );
        }

        throw new BusinessValidationException("Invalid email or password.");
    }
}