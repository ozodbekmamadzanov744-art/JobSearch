package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.RespondedApplicant;

import java.util.List;

public interface RespondedApplicantService {

    RespondedApplicant createResponse(RespondedApplicant response);

    List<RespondedApplicant> findByVacancyId(Long vacancyId);

    List<RespondedApplicant> findByResumeId(Long resumeId);
}