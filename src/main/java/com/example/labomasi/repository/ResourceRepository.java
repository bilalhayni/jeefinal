package com.example.labomasi.repository;

import com.example.labomasi.model.entity.Resource;
import com.example.labomasi.model.enums.ResourceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Page<Resource> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Resource> findByAvailability(ResourceStatus availability);
}