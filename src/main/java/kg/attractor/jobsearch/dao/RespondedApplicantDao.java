package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.RespondedApplicant;

import java.util.List;
import java.util.Optional;

public interface RespondedApplicantDao {

    RespondedApplicant save(RespondedApplicant respondedApplicant);

    Optional<RespondedApplicant> findById(Long id);

    List<RespondedApplicant> findByVacancyId(Long vacancyId);

    List<RespondedApplicant> findByResumeId(Long resumeId);

    boolean existsByResumeIdAndVacancyId(Long resumeId, Long vacancyId);
}