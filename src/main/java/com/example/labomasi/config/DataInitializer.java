package com.example.labomasi.config;

import com.example.labomasi.model.entity.Member;
import com.example.labomasi.model.entity.Role;
import com.example.labomasi.repository.MemberRepository;
import com.example.labomasi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Create roles if not exist
            createRoleIfNotExist("ADMINISTRATEUR");
            createRoleIfNotExist("DIRECTEUR");
            createRoleIfNotExist("ENSEIGNANT");
            createRoleIfNotExist("DOCTORANT");

            // Create admin user if not exist
            createAdminIfNotExist();

            log.info("Data initialization completed!");
        };
    }

    private void createRoleIfNotExist(String roleName) {
        if (!roleRepository.existsByRolename(roleName)) {
            Role role = Role.builder()
                    .rolename(roleName)
                    .build();
            roleRepository.save(role);
            log.info("Role created: {}", roleName);
        }
    }

    private void createAdminIfNotExist() {
        String adminUsername = "admin";

        if (!memberRepository.existsByUsername(adminUsername)) {
            Role adminRole = roleRepository.findByRolename("ADMINISTRATEUR")
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            Member admin = Member.builder()
                    .fname("Admin")
                    .lname("SYSTEM")
                    .username(adminUsername)
                    .email("admin@labomasi.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("")
                    .createdAt(LocalDate.now())
                    .role(adminRole)
                    .build();

            //admin.getRoles().add(adminRole);
            memberRepository.save(admin);

            log.info("=================================");
            log.info("Admin user created!");
            log.info("Username: admin");
            log.info("Password: admin123");
            log.info("=================================");
        }
    }
}