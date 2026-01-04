package com.example.labomasi.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private Long roleId;
}