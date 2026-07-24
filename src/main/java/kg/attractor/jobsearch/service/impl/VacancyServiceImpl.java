package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.RespondedApplicantDao;
import kg.attractor.jobsearch.dao.ResumeDao;
import kg.attractor.jobsearch.dao.UserDao;
import kg.attractor.jobsearch.dao.VacancyDao;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyDao vacancyDao;
    private final RespondedApplicantDao respondedApplicantDao;
    private final ResumeDao resumeDao;
    private final UserDao userDao;

    @Override
    public Vacancy createVacancy(Vacancy vacancy) {
        if (vacancy.getIsActive() == null) {
            vacancy.setIsActive(true);
        }
        return vacancyDao.save(vacancy);
    }

    @Override
    public Vacancy updateVacancy(Long id, Vacancy vacancy) {
        Vacancy existing = getVacancyById(id);
        vacancy.setId(existing.getId());
        vacancy.setAuthorId(existing.getAuthorId());
        vacancyDao.update(vacancy);
        return vacancy;
    }

    @Override
    public void deleteVacancy(Long id) {
        getVacancyById(id);
        vacancyDao.delete(id);
    }

    @Override
    public Vacancy getVacancyById(Long id) {
        return vacancyDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Вакансия с id " + id + " не найдена"));
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
        getVacancyById(vacancyId);

        response.setVacancyId(vacancyId);

        if (respondedApplicantDao.existsByResumeIdAndVacancyId(response.getResumeId(), vacancyId)) {
            throw new IllegalStateException("Соискатель уже откликнулся на эту вакансию этим резюме");
        }

        return respondedApplicantDao.save(response);
    }

    @Override
    public List<User> getApplicantsForVacancy(Long vacancyId) {
        return respondedApplicantDao.findByVacancyId(vacancyId).stream()
                .map(responded -> resumeDao.findById(responded.getResumeId()))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(Resume::getApplicantId)
                .map(userDao::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .toList();
    }

    @Override
    public List<Vacancy> getVacanciesByApplicant(Long applicantId) {
        List<Resume> resumes = resumeDao.findByApplicantId(applicantId);

        return resumes.stream()
                .flatMap(resume -> respondedApplicantDao.findByResumeId(resume.getId()).stream())
                .map(RespondedApplicant::getVacancyId)
                .distinct()
                .map(vacancyDao::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .toList();
    }
}
