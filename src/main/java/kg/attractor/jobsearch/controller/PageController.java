package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.ResumeService;
import kg.attractor.jobsearch.service.UserService;
import kg.attractor.jobsearch.service.VacancyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class PageController {

    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final UserService userService;

    public PageController(ResumeService resumeService, VacancyService vacancyService, UserService userService) {
        this.resumeService = resumeService;
        this.vacancyService = vacancyService;
        this.userService = userService;
    }

    @GetMapping("/vacancies")
    public String vacancies(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("vacancies", vacancyService.getAllActiveVacancies());
        if (userDetails != null) {
            var user = userService.getUserById(userDetails.getUser().getId());
            model.addAttribute("currentUser", user);
            if ("APPLICANT".equals(user.getAccountType())) {
                model.addAttribute("applicantResumes", resumeService.getResumesByApplicant(user.getId()));
            }
        }
        return "vacancies/list";
    }

    @GetMapping("/resumes")
    public String resumes(Model model) {
        model.addAttribute("resumes", resumeService.getAllActiveResumes());
        return "resumes/list";
    }

    @GetMapping("/cabinet")
    public String cabinet(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        var user = userService.getUserById(userDetails.getUser().getId());
        model.addAttribute("user", user);

        if ("APPLICANT".equals(user.getAccountType())) {
            model.addAttribute("resumes", resumeService.getResumesByApplicant(user.getId()));
        } else {
            model.addAttribute("vacancies", vacancyService.getVacanciesByAuthor(user.getId()));
        }

        return "cabinet";
    }
}
