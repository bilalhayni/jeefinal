package com.example.labomasi.service;

import com.example.labomasi.model.entity.Resource;
import com.example.labomasi.model.enums.ResourceStatus;
import com.example.labomasi.repository.ResourceRepository;
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
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    public Page<Resource> findAll(Pageable pageable) {
        return resourceRepository.findAll(pageable);
    }

    public Optional<Resource> findById(Long id) {
        return resourceRepository.findById(id);
    }

    public Page<Resource> searchByName(String keyword, Pageable pageable) {
        return resourceRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public List<Resource> findByAvailability(ResourceStatus status) {
        return resourceRepository.findByAvailability(status);
    }

    public List<Resource> findAvailable() {
        return resourceRepository.findByAvailability(ResourceStatus.AVAILABLE);
    }

    public List<Resource> findUnavailable() {
        return resourceRepository.findByAvailability(ResourceStatus.UNAVAILABLE);
    }

    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    public void deleteById(Long id) {
        resourceRepository.deleteById(id);
    }

    public long count() {
        return resourceRepository.count();
    }

    public long countAvailable() {
        return resourceRepository.findByAvailability(ResourceStatus.AVAILABLE).size();
    }

    public long countUnavailable() {
        return resourceRepository.findByAvailability(ResourceStatus.UNAVAILABLE).size();
    }
}