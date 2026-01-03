package com.example.labomasi.repository;

import com.example.labomasi.model.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Project> findByEndDateIsNull();

    List<Project> findByEndDateIsNotNull();
}