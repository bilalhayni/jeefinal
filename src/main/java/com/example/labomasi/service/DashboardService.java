package com.example.labomasi.service;

import com.example.labomasi.model.enums.ResourceStatus;
import com.example.labomasi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final PublicationRepository publicationRepository;
    private final ResourceRepository resourceRepository;
    private final RoleRepository roleRepository;

    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("totalMembers", memberRepository.count());
        stats.put("totalProjects", projectRepository.count());
        stats.put("totalPublications", publicationRepository.count());
        stats.put("totalResources", resourceRepository.count());
        stats.put("totalRoles", roleRepository.count());

        stats.put("activeProjects", (long) projectRepository.findByEndDateIsNull().size());
        stats.put("completedProjects", (long) projectRepository.findByEndDateIsNotNull().size());

        stats.put("availableResources", (long) resourceRepository.findByAvailability(ResourceStatus.AVAILABLE).size());
        stats.put("unavailableResources", (long) resourceRepository.findByAvailability(ResourceStatus.UNAVAILABLE).size());

        return stats;
    }

    public long getMemberCount() {
        return memberRepository.count();
    }

    public long getProjectCount() {
        return projectRepository.count();
    }

    public long getPublicationCount() {
        return publicationRepository.count();
    }

    public long getResourceCount() {
        return resourceRepository.count();
    }

    public long getActiveProjectCount() {
        return projectRepository.findByEndDateIsNull().size();
    }

    public long getAvailableResourceCount() {
        return resourceRepository.findByAvailability(ResourceStatus.AVAILABLE).size();
    }
}