package securityproject.securityproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    
    private boolean accepted;
    @NotEmpty(message = "Choose the role")
    private String newRole;
    @NotEmpty(message = "Receiver username is required")
    private String receiverUsername;

}
