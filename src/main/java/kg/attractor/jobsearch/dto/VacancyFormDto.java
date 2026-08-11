package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyFormDto {

    @NotBlank(message = "Название вакансии обязательно для заполнения")
    private String name;

    @NotBlank(message = "Описание вакансии обязательно для заполнения")
    private String description;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;

    @NotNull(message = "Зарплата обязательна")
    @Positive(message = "Зарплата должна быть положительным числом")
    private Double salary;

    @NotNull(message = "Минимальный опыт обязателен")
    @PositiveOrZero(message = "Опыт не может быть отрицательным")
    private Integer expFrom;

    @NotNull(message = "Максимальный опыт обязателен")
    @PositiveOrZero(message = "Опыт не может быть отрицательным")
    private Integer expTo;

    private Boolean isActive = true;
}