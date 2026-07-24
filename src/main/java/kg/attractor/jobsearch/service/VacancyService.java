package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;

import java.util.List;

public interface VacancyService {

    Vacancy createVacancy(Vacancy vacancy);

    Vacancy updateVacancy(Long id, Vacancy vacancy);

    void deleteVacancy(Long id);

    Vacancy getVacancyById(Long id);

    List<Vacancy> getAllActiveVacancies();

    List<Vacancy> getVacanciesByCategory(Long categoryId);

    List<Vacancy> getVacanciesByAuthor(Long authorId);

    RespondedApplicant respondToVacancy(Long vacancyId, RespondedApplicant response);

    List<User> getApplicantsForVacancy(Long vacancyId);

    List<Vacancy> getVacanciesByApplicant(Long applicantId);
}