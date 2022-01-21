package com.example.emenu.model.user;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
    @NotNull
    @Email
    private String email;
    @ToString.Exclude
    @NotNull
    private String password;
}
