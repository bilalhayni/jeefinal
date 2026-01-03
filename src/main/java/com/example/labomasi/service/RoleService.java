package com.example.labomasi.service;

import com.example.labomasi.model.entity.Role;
import com.example.labomasi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findByRolename(String rolename) {
        return roleRepository.findByRolename(rolename);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    public boolean existsByRolename(String rolename) {
        return roleRepository.existsByRolename(rolename);
    }

    public long count() {
        return roleRepository.count();
    }
}