package com.filecryptor.controller.dto;

import com.filecryptor.percistence.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserDto {

    private long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private boolean active;
    private String dataOfRegistration;
    private Set<Role> roles;
}
