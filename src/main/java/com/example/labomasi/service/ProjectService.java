package com.example.labomasi.service;

import com.example.labomasi.model.entity.Project;
import com.example.labomasi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public Page<Project> searchByTitle(String keyword, Pageable pageable) {
        return projectRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public List<Project> findActiveProjects() {
        return projectRepository.findByEndDateIsNull();
    }

    public List<Project> findCompletedProjects() {
        return projectRepository.findByEndDateIsNotNull();
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    public long count() {
        return projectRepository.count();
    }

    public long countActive() {
        return projectRepository.findByEndDateIsNull().size();
    }

    public long countCompleted() {
        return projectRepository.findByEndDateIsNotNull().size();
    }
}