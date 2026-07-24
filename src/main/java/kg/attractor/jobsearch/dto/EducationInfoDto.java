package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationInfoDto {

    private Long id;

    @NotBlank(message = "Учебное заведение обязательно для заполнения")
    private String institution;

    @NotBlank(message = "Программа обучения обязательна для заполнения")
    private String program;

    @NotNull(message = "Дата начала обучения обязательна")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank(message = "Степень обязательна для заполнения")
    private String degree;
}