package com.example.labomasi.service;

import com.example.labomasi.model.entity.Member;
import com.example.labomasi.model.entity.Role;
import com.example.labomasi.repository.MemberRepository;
import com.example.labomasi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Member register(Member member, String roleName) {
        // Check if username exists
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email exists
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setCreatedAt(LocalDate.now());

        // Assign role
        Optional<Role> role = roleRepository.findByRolename(roleName);
        role.ifPresent(r -> member.getRoles().add(r));

        return memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void changePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }
}