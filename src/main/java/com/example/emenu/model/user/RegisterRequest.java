package com.example.emenu.model.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @NotNull
    @Email
    @Size(min = 5, max = 30)
    private String email;
    @NotNull
    @Size(min = 5, max = 30)
    private String password;
    @Size(min = 2, max = 30)
    private String firstName;
    @Size(min = 2, max = 30)
    private String lastName;
}
