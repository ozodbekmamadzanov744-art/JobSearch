package kg.attractor.jobsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelRespondedApplicant {

    private Long id;
    private Long resumeId;
    private Long vacancyId;
    private Boolean confirmation;
}