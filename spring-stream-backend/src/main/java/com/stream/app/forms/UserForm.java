package com.stream.app.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForm {

    @NotBlank(message = "Name required")
    private String name;
    @NotBlank(message = "Email required")
    @Email(message = "Enter Valid Email")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 6, message = "Minimum 6 characters long")
    private String password; 
}
