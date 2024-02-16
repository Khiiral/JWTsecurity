package securityproject.securityproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    
    //@NotEmpty(message = "Firstname is required")
    String firstName;
    //@NotEmpty(message = "Last name is required")
    String lastName;
    //@NotEmpty(message = "Email is required")
    //@Email(message = "Invalid email address")
    String email;
    //@NotEmpty(message = "Password is required")
    String password;
    
}
