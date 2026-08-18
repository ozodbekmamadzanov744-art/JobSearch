package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.VacancyFormDto;
import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.*;
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

    @GetMapping
    public String vacancies(Model model,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        model.addAttribute("vacancies", vacancyService.getAllActiveVacancies());

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
        vacancy.setAuthorId(userDetails.getUser().getId());

        vacancyService.createVacancy(vacancy);

        return "redirect:/pages/cabinet";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Vacancy vacancy = vacancyService.getVacancyById(id);

        if (!vacancy.getAuthorId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException(
                    "Вы не являетесь автором этой вакансии"
            );
        }

        VacancyFormDto dto = new VacancyFormDto();

        dto.setName(vacancy.getName());
        dto.setDescription(vacancy.getDescription());
        dto.setCategoryId(vacancy.getCategoryId());
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

        if (!resume.getApplicantId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException(
                    "Нельзя откликнуться чужим резюме"
            );
        }

        RespondedApplicant response = new RespondedApplicant();

        response.setResumeId(resumeId);

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
        vacancy.setCategoryId(dto.getCategoryId());
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

        if (!vacancy.getAuthorId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException(
                    "Вы не являетесь автором этой вакансии"
            );
        }

        List<Resume> resumes = respondedApplicantService
                .findByVacancyId(id)
                .stream()
                .map(RespondedApplicant::getResumeId)
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