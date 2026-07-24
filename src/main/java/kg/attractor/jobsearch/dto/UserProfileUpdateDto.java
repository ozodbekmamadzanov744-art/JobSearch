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

    @NotBlank(message = "Имя обязательно для заполнения")
    private String name;

    private String surname;

    @Min(value = 14, message = "Возраст не может быть меньше 14")
    @Max(value = 100, message = "Возраст не может быть больше 100")
    private Integer age;

    @NotBlank(message = "Номер телефона обязателен для заполнения")
    private String phoneNumber;
}