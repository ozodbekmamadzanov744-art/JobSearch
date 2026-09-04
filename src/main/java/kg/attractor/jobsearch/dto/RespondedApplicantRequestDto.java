package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespondedApplicantRequestDto {

    @NotNull(message = "{validation.resume.id}")
    private Long resumeId;
}
