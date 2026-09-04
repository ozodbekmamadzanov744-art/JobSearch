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

    @NotBlank(message = "{validation.education.institution}")
    private String institution;

    @NotBlank(message = "{validation.education.program}")
    private String program;

    @NotNull(message = "{validation.education.startDate}")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank(message = "{validation.education.degree}")
    private String degree;
}
