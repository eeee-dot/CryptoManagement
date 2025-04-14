package pl.coderslab.cryptomanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long userId;
    @Size(min = 5, max = 50)
    private String username;
    @Email
    private String email;
    @Size(min = 8)
    private String passwordHash;
    private LocalDateTime created;
}
