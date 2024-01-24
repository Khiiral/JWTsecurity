package securityproject.securityproject.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import securityproject.securityproject.models.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    
    @NotEmpty(message = "Firstname is required")
    private String firstName;
    @NotEmpty(message = "Lastname is required")
    private String lastName;
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @Enumerated(EnumType.STRING)
    Role role;
}
