package com.example.labomasi.model.dto.request;

import com.example.labomasi.model.enums.ResourceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequest {

    private String name;
    private String description;
    private ResourceStatus availability;
}