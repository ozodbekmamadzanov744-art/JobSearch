package kg.attractor.jobsearch.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequestDto {

    private Long applicantId;
    private String name;
    private Long categoryId;
    private Double salary;
    private Boolean isActive;
    private List<EducationInfoDto> educationList;
    private List<WorkExperienceInfoDto> workExperienceList;
}