package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pages/companies")
public class CompanyPageController {

    private static final int DEFAULT_PAGE_SIZE = 5;

    private final UserService userService;

    public CompanyPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String companies(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<User> companyPage = userService.getEmployers(page, DEFAULT_PAGE_SIZE);
        model.addAttribute("companyPage", companyPage);
        model.addAttribute("companies", companyPage.getContent());
        return "companies/list";
    }
}
