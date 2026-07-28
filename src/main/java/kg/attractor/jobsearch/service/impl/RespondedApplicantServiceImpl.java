package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.RespondedApplicantDao;
import kg.attractor.jobsearch.exception.DuplicateResponseException;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.service.RespondedApplicantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RespondedApplicantServiceImpl implements RespondedApplicantService {

    private final RespondedApplicantDao respondedApplicantDao;

    @Override
    public RespondedApplicant createResponse(RespondedApplicant response) {
        if (respondedApplicantDao.existsByResumeIdAndVacancyId(response.getResumeId(), response.getVacancyId())) {
            throw new DuplicateResponseException("Соискатель уже откликнулся на эту вакансию этим резюме");
        }
        log.info("Сохранён отклик: резюме id={} -> вакансия id={}", response.getResumeId(), response.getVacancyId());
        return respondedApplicantDao.save(response);
    }

    @Override
    public List<RespondedApplicant> findByVacancyId(Long vacancyId) {
        return respondedApplicantDao.findByVacancyId(vacancyId);
    }

    @Override
    public List<RespondedApplicant> findByResumeId(Long resumeId) {
        return respondedApplicantDao.findByResumeId(resumeId);
    }
}