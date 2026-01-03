package com.example.labomasi.model.dto.response;

import com.example.labomasi.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private LocalDate createdAt;
    private List<String> roles;

    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .firstName(member.getFname())
                .lastName(member.getLname())
                .username(member.getUsername())
                .email(member.getEmail())
                .phone(member.getPhone())
                .createdAt(member.getCreatedAt())
                .roles(member.getRoles().stream()
                        .map(role -> role.getRolename())
                        .toList())
                .build();
    }
}