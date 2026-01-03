package com.example.labomasi.config;

import com.example.labomasi.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Login configuration
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                // Logout configuration
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // Error pages - handled by Spring Boot's error controller
                        .requestMatchers("/error", "/error/**").permitAll()

                        // Member management - ADMINISTRATEUR & DIRECTEUR can view list
                        .requestMatchers("/members").hasAnyRole("ADMINISTRATEUR", "DIRECTEUR")
                        // Member create form - ADMINISTRATEUR only
                        .requestMatchers("/members/new").hasRole("ADMINISTRATEUR")
                        // Member profile view - any authenticated user (could be viewing own profile)
                        .requestMatchers("/members/*").authenticated()
                        // Member edit/delete/roles - ADMINISTRATEUR only
                        .requestMatchers("/members/*/edit").hasRole("ADMINISTRATEUR")
                        .requestMatchers("/members/*/delete").hasRole("ADMINISTRATEUR")
                        .requestMatchers("/members/*/roles/**").hasRole("ADMINISTRATEUR")

                        // Resource management - ADMINISTRATEUR only
                        .requestMatchers("/resource/**").hasRole("ADMINISTRATEUR")

                        // Role management - ADMINISTRATEUR only
                        .requestMatchers("/roles", "/role/**").hasRole("ADMINISTRATEUR")

                        // Project management
                        .requestMatchers("/project/view", "/project/en-cours").authenticated()
                        .requestMatchers("/project/add", "/project/update/**", "/project/delete/**").hasRole("ADMINISTRATEUR")

                        // Publication management
                        .requestMatchers("/pub", "/pub/latest").authenticated()
                        .requestMatchers("/pub/add").hasAnyRole("ENSEIGNANT", "ADMINISTRATEUR")
                        .requestMatchers("/pub/edit/**", "/pub/delete/**").hasRole("ADMINISTRATEUR")

                        // Profile - any authenticated user
                        .requestMatchers("/profile/**").authenticated()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // User details service
                .userDetailsService(userDetailsService)

                // Exception handling - use error templates
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            request.getRequestDispatcher("/error/403").forward(request, response);
                        })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
