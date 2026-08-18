package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.ResumeService;
import kg.attractor.jobsearch.service.UserService;
import kg.attractor.jobsearch.service.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pages/cabinet")
public class CabinetController {

    private static final int DEFAULT_PAGE_SIZE = 5;

    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final UserService userService;

    public CabinetController(ResumeService resumeService, VacancyService vacancyService, UserService userService) {
        this.resumeService = resumeService;
        this.vacancyService = vacancyService;
        this.userService = userService;
    }

    @GetMapping
    public String cabinet(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "date") String sort,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        var user = userService.getUserById(userDetails.getUser().getId());
        model.addAttribute("user", user);

        if ("APPLICANT".equals(user.getAccountType())) {
            Page<Resume> resumePage = resumeService.getResumesByApplicant(user.getId(), page, DEFAULT_PAGE_SIZE);
            model.addAttribute("resumePage", resumePage);
            model.addAttribute("resumes", resumePage.getContent());
        } else {
            Page<Vacancy> vacancyPage = vacancyService.getVacanciesByAuthor(user.getId(), page, DEFAULT_PAGE_SIZE, sort);
            model.addAttribute("vacancyPage", vacancyPage);
            model.addAttribute("vacancies", vacancyPage.getContent());
            model.addAttribute("currentSort", sort);
        }

        return "cabinet";
    }
}
