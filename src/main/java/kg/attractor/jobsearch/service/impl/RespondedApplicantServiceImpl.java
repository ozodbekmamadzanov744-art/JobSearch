package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.DuplicateResponseException;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.repository.RespondedApplicantRepository;
import kg.attractor.jobsearch.service.RespondedApplicantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RespondedApplicantServiceImpl implements RespondedApplicantService {

    private final RespondedApplicantRepository respondedApplicantRepository;

    @Override
    public RespondedApplicant createResponse(RespondedApplicant response) {
        Long resumeId = response.getResume() != null ? response.getResume().getId() : null;
        Long vacancyId = response.getVacancy() != null ? response.getVacancy().getId() : null;

        if (respondedApplicantRepository.existsByResumeIdAndVacancyId(resumeId, vacancyId)) {
            throw new DuplicateResponseException("Соискатель уже откликнулся на эту вакансию этим резюме");
        }
        log.info("Сохранён отклик: резюме id={} -> вакансия id={}", resumeId, vacancyId);
        return respondedApplicantRepository.save(response);
    }

    @Override
    public List<RespondedApplicant> findByVacancyId(Long vacancyId) {
        return respondedApplicantRepository.findByVacancyId(vacancyId);
    }

    @Override
    public List<RespondedApplicant> findByResumeId(Long resumeId) {
        return respondedApplicantRepository.findByResumeId(resumeId);
    }
}