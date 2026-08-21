package kg.attractor.jobsearch.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.UserRegistrationDto;
import kg.attractor.jobsearch.exception.EmailAlreadyExistsException;
import kg.attractor.jobsearch.mapper.UserMapper;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/pages/auth")
public class AuthPageController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthPageController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationDto", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationDto") UserRegistrationDto dto,
                           BindingResult bindingResult,
                           @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                           Model model,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        User saved;
        try {
            User user = UserMapper.toModel(dto);
            saved = userService.register(user);

            if (avatarFile != null && !avatarFile.isEmpty()) {
                userService.uploadAvatar(saved.getId(), avatarFile, saved.getId());
            }
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("generalError", e.getMessage());
            return "auth/register";
        }

        authenticateAndCreateSession(saved.getEmail(), dto.getPassword(), request, response);

        boolean isEmployer = "EMPLOYER".equals(saved.getAccountType());
        return "redirect:" + (isEmployer ? "/pages/resumes" : "/pages/vacancies");
    }

    private void authenticateAndCreateSession(String email, String rawPassword,
                                              HttpServletRequest request, HttpServletResponse response) {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(email, rawPassword);
        Authentication authResult = authenticationManager.authenticate(authRequest);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository().saveContext(context, request, response);
    }
}