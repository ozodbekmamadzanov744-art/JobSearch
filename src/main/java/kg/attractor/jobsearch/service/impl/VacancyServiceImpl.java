package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.VacancyDao;
import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.service.RespondedApplicantService;
import kg.attractor.jobsearch.service.ResumeService;
import kg.attractor.jobsearch.service.UserService;
import kg.attractor.jobsearch.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyDao vacancyDao;
    private final RespondedApplicantService respondedApplicantService;
    private final ResumeService resumeService;
    private final UserService userService;

    @Override
    public Vacancy createVacancy(Vacancy vacancy) {
        log.info("Создание вакансии '{}' от работодателя id={}", vacancy.getName(), vacancy.getAuthorId());
        if (vacancy.getIsActive() == null) {
            vacancy.setIsActive(true);
        }
        return vacancyDao.save(vacancy);
    }

    @Override
    public Vacancy updateVacancy(Long id, Vacancy vacancy, Long currentUserId) {
        Vacancy existing = getVacancyById(id);

        if (!existing.getAuthorId().equals(currentUserId)) {
            throw new ForbiddenOperationException("Вы не являетесь автором этой вакансии");
        }

        vacancy.setId(existing.getId());
        vacancy.setAuthorId(existing.getAuthorId());
        vacancyDao.update(vacancy);
        return vacancy;
    }

    @Override
    public void deleteVacancy(Long id, Long currentUserId) {
        Vacancy existing = getVacancyById(id);

        if (!existing.getAuthorId().equals(currentUserId)) {
            throw new ForbiddenOperationException("Вы не являетесь автором этой вакансии");
        }

        vacancyDao.delete(id);
    }

    @Override
    public Vacancy getVacancyById(Long id) {
        return vacancyDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Вакансия с id " + id + " не найдена"));
    }

    @Override
    public List<Vacancy> getAllActiveVacancies() {
        return vacancyDao.findAll().stream()
                .filter(vacancy -> Boolean.TRUE.equals(vacancy.getIsActive()))
                .toList();
    }

    @Override
    public List<Vacancy> getVacanciesByCategory(Long categoryId) {
        return vacancyDao.findByCategoryId(categoryId).stream()
                .filter(vacancy -> Boolean.TRUE.equals(vacancy.getIsActive()))
                .toList();
    }

    @Override
    public List<Vacancy> getVacanciesByAuthor(Long authorId) {
        return vacancyDao.findByAuthorId(authorId);
    }

    @Override
    public RespondedApplicant respondToVacancy(Long vacancyId, RespondedApplicant response) {
        log.info("Соискатель откликается резюме id={} на вакансию id={}", response.getResumeId(), vacancyId);
        getVacancyById(vacancyId);
        response.setVacancyId(vacancyId);
        return respondedApplicantService.createResponse(response);
    }

    @Override
    public List<User> getApplicantsForVacancy(Long vacancyId) {
        return respondedApplicantService.findByVacancyId(vacancyId).stream()
                .map(RespondedApplicant::getResumeId)
                .map(resumeService::getResumeById)
                .map(Resume::getApplicantId)
                .map(userService::getUserById)
                .toList();
    }

    @Override
    public List<Vacancy> getVacanciesByApplicant(Long applicantId) {
        return resumeService.getResumesByApplicant(applicantId).stream()
                .flatMap(resume -> respondedApplicantService.findByResumeId(resume.getId()).stream())
                .map(RespondedApplicant::getVacancyId)
                .distinct()
                .map(this::getVacancyById)
                .toList();
    }
}