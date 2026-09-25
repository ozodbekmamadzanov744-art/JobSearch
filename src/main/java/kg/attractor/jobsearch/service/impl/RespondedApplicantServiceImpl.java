package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.DuplicateResponseException;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.repository.RespondedApplicantRepository;
import kg.attractor.jobsearch.service.RespondedApplicantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            throw new DuplicateResponseException("error.response.duplicate");
        }
        log.info("Сохранён отклик: резюме id={} -> вакансия id={}", resumeId, vacancyId);
        return respondedApplicantRepository.save(response);
    }

    @Override
    public RespondedApplicant findOrCreateResponse(RespondedApplicant response) {
        Long resumeId = response.getResume() != null ? response.getResume().getId() : null;
        Long vacancyId = response.getVacancy() != null ? response.getVacancy().getId() : null;

        return respondedApplicantRepository.findByResumeIdAndVacancyId(resumeId, vacancyId)
                .orElseGet(() -> createResponse(response));
    }

    @Override
    public List<RespondedApplicant> findByVacancyId(Long vacancyId) {
        return respondedApplicantRepository.findByVacancyId(vacancyId);
    }

    @Override
    public List<RespondedApplicant> findByResumeId(Long resumeId) {
        return respondedApplicantRepository.findByResumeId(resumeId);
    }

    @Override
    public List<RespondedApplicant> findByApplicantId(Long applicantId) {
        return respondedApplicantRepository.findByResumeApplicantId(applicantId);
    }

    @Override
    public Optional<RespondedApplicant> findByResumeIdAndVacancyId(Long resumeId, Long vacancyId) {
        return respondedApplicantRepository.findByResumeIdAndVacancyId(resumeId, vacancyId);
    }

    @Override
    public RespondedApplicant getById(Long id) {
        return respondedApplicantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.notFound.response"));
    }
}
