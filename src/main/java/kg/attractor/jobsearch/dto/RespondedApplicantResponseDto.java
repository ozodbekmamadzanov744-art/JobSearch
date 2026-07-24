package kg.attractor.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespondedApplicantResponseDto {

    private Long id;
    private Long resumeId;
    private Long vacancyId;
    private Boolean confirmation;
}