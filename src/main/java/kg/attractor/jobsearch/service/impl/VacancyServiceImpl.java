package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.repository.VacancyRepository;
import kg.attractor.jobsearch.service.RespondedApplicantService;
import kg.attractor.jobsearch.service.ResumeService;
import kg.attractor.jobsearch.service.UserService;
import kg.attractor.jobsearch.service.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private static final String SORT_BY_RESPONSES = "responses";
    private static final String SORT_BY_RESPONSES_ASC = "responses_asc";
    private static final String SORT_BY_RESPONSES_DESC = "responses_desc";
    private static final String SORT_BY_DATE = "date";
    private static final String SORT_BY_DATE_ASC = "date_asc";
    private static final String SORT_BY_DATE_DESC = "date_desc";

    private final VacancyRepository vacancyRepository;
    private final RespondedApplicantService respondedApplicantService;
    private final ResumeService resumeService;
    private final UserService userService;

    @Override
    public Vacancy createVacancy(Vacancy vacancy) {
        Long authorId = vacancy.getAuthor() != null ? vacancy.getAuthor().getId() : null;
        log.info("Создание вакансии '{}' от работодателя id={}", vacancy.getName(), authorId);
        if (vacancy.getIsActive() == null) {
            vacancy.setIsActive(true);
        }
        return vacancyRepository.save(vacancy);
    }

    @Override
    public Vacancy updateVacancy(Long id, Vacancy vacancy, Long currentUserId) {
        Vacancy existing = getVacancyById(id);

        if (existing.getAuthor() == null || !existing.getAuthor().getId().equals(currentUserId)) {
            throw new ForbiddenOperationException("error.vacancy.owner");
        }

        existing.setName(vacancy.getName());
        existing.setDescription(vacancy.getDescription());
        existing.setCategory(vacancy.getCategory());
        existing.setSalary(vacancy.getSalary());
        existing.setExpFrom(vacancy.getExpFrom());
        existing.setExpTo(vacancy.getExpTo());
        existing.setIsActive(vacancy.getIsActive());

        return vacancyRepository.save(existing);
    }

    @Override
    public void deleteVacancy(Long id, Long currentUserId) {
        Vacancy existing = getVacancyById(id);

        if (existing.getAuthor() == null || !existing.getAuthor().getId().equals(currentUserId)) {
            throw new ForbiddenOperationException("error.vacancy.owner");
        }

        vacancyRepository.deleteById(id);
    }

    @Override
    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.notFound.vacancy"));
    }

    @Override
    public List<Vacancy> getAllActiveVacancies() {
        return vacancyRepository.findByIsActiveTrue(Pageable.unpaged()).getContent();
    }

    @Override
    public List<Vacancy> getVacanciesByCategory(Long categoryId) {
        return vacancyRepository.findByCategoryId(categoryId).stream()
                .filter(vacancy -> Boolean.TRUE.equals(vacancy.getIsActive()))
                .toList();
    }

    @Override
    public List<Vacancy> getVacanciesByAuthor(Long authorId) {
        return vacancyRepository.findByAuthorId(authorId, Pageable.unpaged()).getContent();
    }

    @Override
    public RespondedApplicant respondToVacancy(Long vacancyId, RespondedApplicant response) {
        Long resumeId = response.getResume() != null ? response.getResume().getId() : null;
        log.info("Соискатель откликается резюме id={} на вакансию id={}", resumeId, vacancyId);
        Vacancy vacancy = getVacancyById(vacancyId);
        response.setVacancy(vacancy);
        return respondedApplicantService.createResponse(response);
    }

    @Override
    public List<User> getApplicantsForVacancy(Long vacancyId) {
        return respondedApplicantService.findByVacancyId(vacancyId).stream()
                .map(RespondedApplicant::getResume)
                .map(Resume::getId)
                .map(resumeService::getResumeById)
                .map(Resume::getApplicant)
                .map(User::getId)
                .map(userService::getUserById)
                .toList();
    }

    @Override
    public List<Vacancy> getVacanciesByApplicant(Long applicantId) {
        return resumeService.getResumesByApplicant(applicantId).stream()
                .flatMap(resume -> respondedApplicantService.findByResumeId(resume.getId()).stream())
                .map(RespondedApplicant::getVacancy)
                .map(Vacancy::getId)
                .distinct()
                .map(this::getVacancyById)
                .toList();
    }

    @Override
    public Page<Vacancy> getActiveVacancies(int page, int size, String sortBy) {
        if (SORT_BY_RESPONSES.equals(sortBy) || SORT_BY_RESPONSES_DESC.equals(sortBy)) {
            return vacancyRepository.findActiveOrderByResponseCountDesc(PageRequest.of(page, size));
        }
        if (SORT_BY_RESPONSES_ASC.equals(sortBy)) {
            return vacancyRepository.findActiveOrderByResponseCountAsc(PageRequest.of(page, size));
        }
        Sort.Direction direction = SORT_BY_DATE_ASC.equals(sortBy) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdDate"));
        return vacancyRepository.findByIsActiveTrue(pageable);
    }

    @Override
    public Page<Vacancy> getVacanciesByAuthor(Long authorId, int page, int size, String sortBy) {
        if (SORT_BY_RESPONSES.equals(sortBy) || SORT_BY_RESPONSES_DESC.equals(sortBy)) {
            return vacancyRepository.findByAuthorIdOrderByResponseCountDesc(authorId, PageRequest.of(page, size));
        }
        if (SORT_BY_RESPONSES_ASC.equals(sortBy)) {
            return vacancyRepository.findByAuthorIdOrderByResponseCountAsc(authorId, PageRequest.of(page, size));
        }
        Sort.Direction direction = SORT_BY_DATE_ASC.equals(sortBy) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdDate"));
        return vacancyRepository.findByAuthorId(authorId, pageable);
    }
}
