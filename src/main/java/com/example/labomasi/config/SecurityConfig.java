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

                        // Error pages
                        .requestMatchers("/404", "/403", "/error").permitAll()

                        // Member management - ADMINISTRATEUR & DIRECTEUR can view
                        .requestMatchers("/member").hasAnyRole("ADMINISTRATEUR", "DIRECTEUR")
                        .requestMatchers("/member/add", "/member/edit/**", "/member/delete/**", "/member/addRole/**", "/member/*/removeRole/**").hasRole("ADMINISTRATEUR")

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

                // Access denied handler
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/403")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}