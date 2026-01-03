package com.example.labomasi.service;

import com.example.labomasi.model.entity.Publication;
import com.example.labomasi.repository.PublicationRepository;
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
public class PublicationService {

    private final PublicationRepository publicationRepository;

    public List<Publication> findAll() {
        return publicationRepository.findAll();
    }

    public Page<Publication> findAll(Pageable pageable) {
        return publicationRepository.findAll(pageable);
    }

    public Optional<Publication> findById(Long id) {
        return publicationRepository.findById(id);
    }

    public Page<Publication> searchByTitle(String keyword, Pageable pageable) {
        return publicationRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public List<Publication> findByProjectId(Long projectId) {
        return publicationRepository.findByProjectId(projectId);
    }

    public List<Publication> findByAuthor(String author) {
        return publicationRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Publication> findWithoutProject() {
        return publicationRepository.findByProjectIsNull();
    }

    public List<Publication> findWithProject() {
        return publicationRepository.findByProjectIsNotNull();
    }

    public Publication save(Publication publication) {
        return publicationRepository.save(publication);
    }

    public void deleteById(Long id) {
        publicationRepository.deleteById(id);
    }

    public long count() {
        return publicationRepository.count();
    }
}