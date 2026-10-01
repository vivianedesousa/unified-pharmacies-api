package com.farmacies.unifiedpharmacies.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        private final JwtFilter jwtFilter;

        public SecurityConfig(JwtFilter jwtFilter) {
                this.jwtFilter = jwtFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                HttpSecurity http
        ) throws Exception {

                http
                        // =========================
                        // CSRF
                        // =========================

                        .csrf(csrf -> csrf.disable())

                        // =========================
                        // SESSION
                        // =========================

                        .sessionManagement(session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                        )

                        // =========================
                        // AUTHORIZATION
                        // =========================

                        .authorizeHttpRequests(auth -> auth

                                // =========================
                                // AUTHENTICATION
                                // =========================

                                .requestMatchers(
                                        "/api/v1/auth/**"
                                ).permitAll()

                                // =========================
                                // PATIENT REGISTRATION
                                // =========================

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/patients"
                                ).permitAll()

                                // =========================
                                // SWAGGER / OPENAPI
                                // =========================

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**"
                                ).permitAll()

                                // =========================
                                // USERS
                                // =========================

                                .requestMatchers(
                                        "/api/v1/users/**"
                                ).hasRole("SYSTEM_ADMIN")

                                // =========================
                                // PHARMACIES
                                // =========================

                                // Criar farmácia
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/pharmacies"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER"
                                )

                                // Listar todas as farmácias
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/pharmacies"
                                ).hasRole("SYSTEM_ADMIN")

                                // Consultar farmácia por CNPJ
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/pharmacies/cnpj/*"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST"
                                )

                                // Consultar farmácia por ID
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/pharmacies/*"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST"
                                )

                                // Reativar farmácia
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/pharmacies/*/reactivate"
                                ).hasRole("SYSTEM_ADMIN")

                                // Atualizar farmácia
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/pharmacies/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER"
                                )

                                // Desativar farmácia
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/pharmacies/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER"
                                )

                                // =========================
                                // MEDICATIONS
                                // =========================

                                // Criar medicamento
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/medications"
                                ).hasRole("SYSTEM_ADMIN")

                                // Consultar medicamentos
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/medications/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST"
                                )

                                // Atualizar medicamento
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/medications/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER"
                                )

                                // =========================
                                // MEDICATION EANS
                                // =========================

                                // Criar EAN
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/medication-eans"
                                ).hasRole("SYSTEM_ADMIN")

                                // Consultar EANs
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/medication-eans/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST"
                                )

                                // Atualizar EAN
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/medication-eans/**"
                                ).hasRole("SYSTEM_ADMIN")

                                // Excluir EAN
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/medication-eans/**"
                                ).hasRole("SYSTEM_ADMIN")

                                // =========================
                                // MEDICATION AVAILABILITY
                                // =========================

                                // Informar disponibilidade
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/medication-availability"
                                ).hasAnyRole(
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST"
                                )

                                // Consultar disponibilidade
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/medication-availability/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST"
                                )

                                // Atualizar disponibilidade
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/medication-availability/**"
                                ).hasAnyRole(
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST"
                                )

                                // Excluir disponibilidade
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/medication-availability/**"
                                ).hasRole("PHARMACY_MANAGER")

                                // =========================
                                // PATIENTS
                                // =========================

                                // Listar pacientes
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/patients"
                                ).hasRole("SYSTEM_ADMIN")

                                // Consultar paciente por ID
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/patients/*"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PATIENT"
                                )

                                // Reativar paciente
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/patients/*/reactivate"
                                ).hasRole("SYSTEM_ADMIN")

                                // Atualizar paciente
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/patients/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PATIENT"
                                )

                                // Alterar CPF
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/v1/patients/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PATIENT"
                                )

                                // Excluir / desativar paciente
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/patients/**"
                                ).hasRole("SYSTEM_ADMIN")

                                // =========================
                                // PRESCRIPTIONS
                                // =========================

                                // Listar prescrições
                                // Paciente não pode listar todas
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/prescriptions"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST"
                                )

                                // Consultar prescrição por ID
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/prescriptions/*"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST",
                                        "PATIENT"
                                )

                                // Atualizar prescrição
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/prescriptions/**"
                                ).hasRole("PHARMACIST")

                                // =========================
                                // REQUESTS
                                // =========================

                                // Criar solicitação
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/requests"
                                ).hasRole("PATIENT")

                                // Consultar solicitação
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/requests/**"
                                ).hasAnyRole(
                                        "SYSTEM_ADMIN",
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST",
                                        "PATIENT"
                                )

                                // Atualizar solicitação
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/requests/**"
                                ).hasAnyRole(
                                        "PHARMACY_MANAGER",
                                        "PHARMACIST",
                                        "PATIENT"
                                )

                                // Análise do medicamento
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/v1/requests/*/medication-analysis"
                                ).hasRole("PHARMACIST")

                                // =========================
                                // ANY OTHER ENDPOINT
                                // =========================

                                .anyRequest().authenticated()
                        )

                        // =========================
                        // JWT FILTER
                        // =========================

                        .addFilterBefore(
                                jwtFilter,
                                UsernamePasswordAuthenticationFilter.class
                        );

                return http.build();
        }
}