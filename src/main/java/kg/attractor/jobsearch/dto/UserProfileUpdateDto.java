package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {

    @NotBlank(message = "{validation.user.name}")
    private String name;

    private String surname;

    @Min(value = 14, message = "{validation.user.age.min}")
    @Max(value = 100, message = "{validation.user.age.max}")
    private Integer age;

    @NotBlank(message = "{validation.user.phone}")
    private String phoneNumber;
}
