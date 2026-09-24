package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordResetDto {

    @NotBlank(message = "{validation.reset.token}")
    private String token;

    @NotBlank(message = "{validation.user.password}")
    @Size(min = 6, message = "{validation.user.password.size}")
    private String password;
}