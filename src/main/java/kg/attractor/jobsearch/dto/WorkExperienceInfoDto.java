package kg.attractor.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceInfoDto {

    private Long id;
    private Integer years;
    private String companyName;
    private String position;
    private String responsibilities;
}