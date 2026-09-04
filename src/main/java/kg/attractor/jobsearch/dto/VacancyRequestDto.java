package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyRequestDto {

    @NotBlank(message = "{validation.vacancy.name}")
    private String name;

    @NotBlank(message = "{validation.vacancy.description}")
    private String description;

    @NotNull(message = "{validation.vacancy.category}")
    private Long categoryId;

    @NotNull(message = "{validation.vacancy.salary}")
    @Positive(message = "{validation.resume.salary.positive}")
    private Double salary;

    @NotNull(message = "{validation.vacancy.experience.from}")
    @PositiveOrZero(message = "{validation.vacancy.experience}")
    private Integer expFrom;

    @NotNull(message = "{validation.vacancy.experience.to}")
    @PositiveOrZero(message = "{validation.vacancy.experience}")
    private Integer expTo;

    private Boolean isActive;

    @NotNull(message = "{validation.vacancy.authorId}")
    private Long authorId;
}
