package securityproject.securityproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    String email;
    @NotEmpty(message = "Password is required")
    String password;
}
