package kg.attractor.jobsearch.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequestDto {

    @NotNull(message = "Id соискателя обязателен")
    private Long applicantId;

    @NotBlank(message = "Название резюме обязательно для заполнения")
    private String name;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;

    @NotNull(message = "Ожидаемая зарплата обязательна")
    @Positive(message = "Зарплата должна быть положительным числом")
    private Double salary;

    private Boolean isActive;

    @Valid
    private List<EducationInfoDto> educationList;

    @Valid
    private List<WorkExperienceInfoDto> workExperienceList;
}