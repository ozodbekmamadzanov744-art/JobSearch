package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.RespondedApplicant;

import java.util.List;
import java.util.Optional;

public interface RespondedApplicantService {

    RespondedApplicant createResponse(RespondedApplicant response);

    RespondedApplicant findOrCreateResponse(RespondedApplicant response);

    List<RespondedApplicant> findByVacancyId(Long vacancyId);

    List<RespondedApplicant> findByResumeId(Long resumeId);

    List<RespondedApplicant> findByApplicantId(Long applicantId);

    Optional<RespondedApplicant> findByResumeIdAndVacancyId(Long resumeId, Long vacancyId);

    RespondedApplicant getById(Long id);
}
