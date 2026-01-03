package com.example.labomasi.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {

    private long totalMembers;
    private long totalProjects;
    private long totalPublications;
    private long totalResources;
    private long activeProjects;
    private long completedProjects;
    private long availableResources;
    private long unavailableResources;
}