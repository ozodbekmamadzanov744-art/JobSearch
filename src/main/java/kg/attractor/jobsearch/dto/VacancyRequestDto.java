package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyRequestDto {

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

    private Boolean isActive;

    @NotNull(message = "Id автора вакансии обязателен")
    private Long authorId;
}