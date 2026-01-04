package com.example.labomasi.service;

import com.example.labomasi.exception.BadRequestException;
import com.example.labomasi.exception.ResourceNotFoundException;
import com.example.labomasi.model.dto.form.MemberCreateForm;
import com.example.labomasi.model.dto.form.MemberEditForm;
import com.example.labomasi.model.entity.Member;
import com.example.labomasi.model.entity.Role;
import com.example.labomasi.repository.MemberRepository;
import com.example.labomasi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member getByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "username", username));
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Page<Member> searchByLastName(String keyword, Pageable pageable) {
        return memberRepository.findByLnameContainingIgnoreCase(keyword, pageable);
    }

    /**
     * Creates a new member from the form data.
     * Business logic: validates uniqueness, encodes password, sets uppercase lastname, assigns role.
     */
    public Member createMember(MemberCreateForm form) {
        // Validate username uniqueness
        if (memberRepository.existsByUsername(form.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        // Validate email uniqueness
        if (memberRepository.existsByEmail(form.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Validate password confirmation
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Find the role
        Role role = roleRepository.findById(form.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", form.getRoleId()));

        // Build the member entity
        Member member = Member.builder()
                .fname(form.getFirstName())
                .lname(form.getLastName().toUpperCase()) // Uppercase lastname
                .username(form.getUsername())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .phone(form.getPhone())
                .createdAt(LocalDate.now())
                .role(role)
                .build();

        return memberRepository.save(member);
    }

    /**
     * Updates an existing member.
     * Business logic: validates email if changed, encodes password if provided, uppercase lastname, updates role.
     */
    public Member updateMember(String username, MemberEditForm form) {
        Member member = getByUsername(username);

        // Check email uniqueness if changed
        if (!member.getEmail().equals(form.getEmail()) && memberRepository.existsByEmail(form.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        member.setFname(form.getFirstName());
        member.setLname(form.getLastName().toUpperCase()); // Uppercase lastname
        member.setEmail(form.getEmail());
        member.setPhone(form.getPhone());

        // Only update password if provided
        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        // Update role if provided
        if (form.getRoleId() != null) {
            Role role = roleRepository.findById(form.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", form.getRoleId()));
            member.setRole(role);
        }

        return memberRepository.save(member);
    }

    /**
     * Deletes a member by username.
     */
    public void deleteByUsername(String username) {
        Member member = getByUsername(username);
        memberRepository.delete(member);
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            member.setCreatedAt(LocalDate.now());
        }
        return memberRepository.save(member);
    }

    public Member update(Member member) {
        return memberRepository.save(member);
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public long count() {
        return memberRepository.count();
    }
}
