package securityproject.securityproject.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private Long id;
    
    private String firstName;

    private String lastName;

    private String email;

    @Enumerated(EnumType.STRING)
    Role role;
}
