package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "{validation.user.name}")
    private String name;

    private String surname;

    @Min(value = 14, message = "{validation.user.age.min}")
    @Max(value = 100, message = "{validation.user.age.max}")
    private Integer age;

    @NotBlank(message = "{validation.user.email}")
    @Email(message = "{validation.user.email.format}")
    private String email;

    @NotBlank(message = "{validation.user.password}")
    @Size(min = 6, message = "{validation.user.password.size}")
    private String password;

    @NotBlank(message = "{validation.user.phone}")
    private String phoneNumber;

    private String avatar;

    @NotBlank(message = "{validation.user.accountType}")
    @Pattern(regexp = "APPLICANT|EMPLOYER", message = "{validation.user.accountType.pattern}")
    private String accountType;
}
