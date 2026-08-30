package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.VacancyFormDto;
import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.model.Category;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pages/vacancies")
public class VacancyPageController {

    private final VacancyService vacancyService;
    private final CategoryService categoryService;
    private final RespondedApplicantService respondedApplicantService;
    private final ResumeService resumeService;
    private final UserService userService;

    public VacancyPageController(VacancyService vacancyService,
                                 CategoryService categoryService,
                                 RespondedApplicantService respondedApplicantService,
                                 ResumeService resumeService,
                                 UserService userService) {
        this.vacancyService = vacancyService;
        this.categoryService = categoryService;
        this.respondedApplicantService = respondedApplicantService;
        this.resumeService = resumeService;
        this.userService = userService;
    }

    private static final int DEFAULT_PAGE_SIZE = 5;

    @GetMapping
    public String vacancies(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "date") String sort,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<Vacancy> vacancyPage = vacancyService.getActiveVacancies(page, DEFAULT_PAGE_SIZE, sort);

        model.addAttribute("vacancyPage", vacancyPage);
        model.addAttribute("vacancies", vacancyPage.getContent());
        model.addAttribute("currentSort", sort);

        if (userDetails != null) {
            var user = userService.getUserById(userDetails.getUser().getId());

            model.addAttribute("currentUser", user);

            if ("APPLICANT".equals(user.getAccountType())) {
                model.addAttribute(
                        "applicantResumes",
                        resumeService.getResumesByApplicant(user.getId())
                );
            }
        }

        return "vacancies/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         Model model,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Vacancy vacancy = vacancyService.getVacancyById(id);
        model.addAttribute("vacancy", vacancy);

        if (userDetails != null) {
            var user = userService.getUserById(userDetails.getUser().getId());
            model.addAttribute("currentUser", user);
            if ("APPLICANT".equals(user.getAccountType())) {
                model.addAttribute("applicantResumes", resumeService.getResumesByApplicant(user.getId()));
            }
        }

        return "vacancies/detail";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("vacancyDto", new VacancyFormDto());
        addReferenceData(model);

        return "vacancies/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("vacancyDto") VacancyFormDto dto,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         Model model) {

        // Проверяем, что опыт "от" не больше опыта "до"
        if (dto.getExpFrom() != null
                && dto.getExpTo() != null
                && dto.getExpFrom() > dto.getExpTo()) {

            bindingResult.rejectValue(
                    "expFrom",
                    "expFrom.invalid",
                    "Опыт от не может быть больше чем опыт до"
            );
        }

        if (bindingResult.hasErrors()) {
            addReferenceData(model);
            return "vacancies/form";
        }

        Vacancy vacancy = toModel(dto);
        User author = new User();
        author.setId(userDetails.getUser().getId());
        vacancy.setAuthor(author);

        vacancyService.createVacancy(vacancy);

        return "redirect:/pages/cabinet";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Vacancy vacancy = vacancyService.getVacancyById(id);

        if (vacancy.getAuthor() == null || !vacancy.getAuthor().getId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException(
                    "Вы не являетесь автором этой вакансии"
            );
        }

        VacancyFormDto dto = new VacancyFormDto();

        dto.setName(vacancy.getName());
        dto.setDescription(vacancy.getDescription());
        dto.setCategoryId(vacancy.getCategory() != null ? vacancy.getCategory().getId() : null);
        dto.setSalary(vacancy.getSalary());
        dto.setExpFrom(vacancy.getExpFrom());
        dto.setExpTo(vacancy.getExpTo());
        dto.setIsActive(vacancy.getIsActive());

        model.addAttribute("vacancyDto", dto);
        model.addAttribute("vacancyId", id);

        addReferenceData(model);

        return "vacancies/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("vacancyDto") VacancyFormDto dto,
                       BindingResult bindingResult,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {

        // Проверяем, что опыт "от" не больше опыта "до"
        if (dto.getExpFrom() != null
                && dto.getExpTo() != null
                && dto.getExpFrom() > dto.getExpTo()) {

            bindingResult.rejectValue(
                    "expFrom",
                    "expFrom.invalid",
                    "Опыт от не может быть больше чем опыт до"
            );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("vacancyId", id);
            addReferenceData(model);

            return "vacancies/form";
        }

        Vacancy vacancy = toModel(dto);

        vacancyService.updateVacancy(
                id,
                vacancy,
                userDetails.getUser().getId()
        );

        return "redirect:/pages/cabinet";
    }

    @PostMapping("/{id}/respond")
    public String respond(@PathVariable Long id,
                          @RequestParam Long resumeId,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        Resume resume = resumeService.getResumeById(resumeId);

        if (resume.getApplicant() == null || !resume.getApplicant().getId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException(
                    "Нельзя откликнуться чужим резюме"
            );
        }

        RespondedApplicant response = new RespondedApplicant();

        Resume resumeReference = new Resume();
        resumeReference.setId(resumeId);
        response.setResume(resumeReference);

        vacancyService.respondToVacancy(id, response);

        return "redirect:/pages/vacancies";
    }

    private void addReferenceData(Model model) {
        Map<String, String> categories = new LinkedHashMap<>();

        categoryService.getAllCategories().forEach(
                category -> categories.put(
                        String.valueOf(category.getId()),
                        category.getName()
                )
        );

        model.addAttribute("categories", categories);
    }

    private Vacancy toModel(VacancyFormDto dto) {
        Vacancy vacancy = new Vacancy();

        vacancy.setName(dto.getName());
        vacancy.setDescription(dto.getDescription());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            vacancy.setCategory(category);
        }

        vacancy.setSalary(dto.getSalary());
        vacancy.setExpFrom(dto.getExpFrom());
        vacancy.setExpTo(dto.getExpTo());
        vacancy.setIsActive(
                dto.getIsActive() == null || dto.getIsActive()
        );

        return vacancy;
    }

    @GetMapping("/{id}/applicants")
    public String applicants(@PathVariable Long id,
                             Model model,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        Vacancy vacancy = vacancyService.getVacancyById(id);

        if (vacancy.getAuthor() == null || !vacancy.getAuthor().getId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException(
                    "Вы не являетесь автором этой вакансии"
            );
        }

        List<Resume> resumes = respondedApplicantService
                .findByVacancyId(id)
                .stream()
                .map(RespondedApplicant::getResume)
                .filter(java.util.Objects::nonNull)
                .map(Resume::getId)
                .map(resumeService::getResumeById)
                .toList();

        model.addAttribute("resumes", resumes);
        model.addAttribute(
                "pageTitle",
                "Отклики на вакансию: " + vacancy.getName()
        );
        model.addAttribute(
                "emptyMessage",
                "Пока никто не откликнулся на эту вакансию."
        );

        return "resumes/list";
    }
}
