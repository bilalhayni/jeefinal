package com.example.labomasi.repository;

import com.example.labomasi.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    boolean existsByName(String name);

    Optional<Department> findByCode(String code);

    boolean existsByCode(String code);
}
