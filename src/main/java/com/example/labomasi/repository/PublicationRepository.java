package com.example.labomasi.repository;

import com.example.labomasi.model.entity.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    Page<Publication> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Publication> findByProjectId(Long projectId);

    List<Publication> findByAuthorContainingIgnoreCase(String author);

    List<Publication> findByProjectIsNull();

    List<Publication> findByProjectIsNotNull();
}