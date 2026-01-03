package com.example.labomasi.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationRequest {

    private String title;
    private String author;
    private LocalDate publicationDate;
    private Long projectId;
}