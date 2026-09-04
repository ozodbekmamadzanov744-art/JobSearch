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

    @NotNull(message = "{validation.workExperience.years}")
    @PositiveOrZero(message = "{validation.workExperience.years.positive}")
    private Integer years;

    @NotBlank(message = "{validation.workExperience.company}")
    private String companyName;

    @NotBlank(message = "{validation.workExperience.position}")
    private String position;

    @NotBlank(message = "{validation.workExperience.responsibilities}")
    private String responsibilities;
}
