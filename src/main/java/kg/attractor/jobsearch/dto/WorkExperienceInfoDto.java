package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceInfoDto {

    private Long id;

    @NotNull(message = "Количество лет опыта обязательно")
    @PositiveOrZero(message = "Количество лет опыта не может быть отрицательным")
    private Integer years;

    @NotBlank(message = "Название компании обязательно для заполнения")
    private String companyName;

    @NotBlank(message = "Должность обязательна для заполнения")
    private String position;

    @NotBlank(message = "Обязанности обязательны для заполнения")
    private String responsibilities;
}