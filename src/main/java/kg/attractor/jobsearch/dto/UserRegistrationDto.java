package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "Имя обязательно для заполнения")
    private String name;

    private String surname;

    @Min(value = 14, message = "Возраст не может быть меньше 14")
    @Max(value = 100, message = "Возраст не может быть больше 100")
    private Integer age;

    @NotBlank(message = "Email обязателен для заполнения")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Пароль обязателен для заполнения")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @NotBlank(message = "Номер телефона обязателен для заполнения")
    private String phoneNumber;

    private String avatar;

    @NotBlank(message = "Тип аккаунта обязателен для заполнения")
    @Pattern(regexp = "APPLICANT|EMPLOYER", message = "Тип аккаунта должен быть APPLICANT или EMPLOYER")
    private String accountType;
}