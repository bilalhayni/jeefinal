package com.example.labomasi.service;

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

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Page<Member> searchByLastName(String keyword, Pageable pageable) {
        return memberRepository.findByLnameContainingIgnoreCase(keyword, pageable);
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

    public void addRoleToMember(Long memberId, Long roleId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        member.getRoles().add(role);
        memberRepository.save(member);
    }

    public void removeRoleFromMember(Long memberId, Long roleId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.getRoles().removeIf(role -> role.getId().equals(roleId));
        memberRepository.save(member);
    }

    public long count() {
        return memberRepository.count();
    }
}