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

    @NotNull(message = "{validation.resume.applicantId}")
    private Long applicantId;

    @NotBlank(message = "{validation.resume.name}")
    private String name;

    @NotNull(message = "{validation.resume.category}")
    private Long categoryId;

    @NotNull(message = "{validation.resume.salary}")
    @Positive(message = "{validation.resume.salary.positive}")
    private Double salary;

    private Boolean isActive;

    @Valid
    private List<EducationInfoDto> educationList;

    @Valid
    private List<WorkExperienceInfoDto> workExperienceList;

    @Valid
    private List<ContactInfoDto> contactList;
}
