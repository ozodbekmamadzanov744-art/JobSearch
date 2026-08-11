package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.UserRegistrationDto;
import kg.attractor.jobsearch.exception.EmailAlreadyExistsException;
import kg.attractor.jobsearch.mapper.UserMapper;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/pages/auth")
public class AuthPageController {

    private final UserService userService;

    public AuthPageController(UserService userService) {
        this.userService = userService;
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
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            User user = UserMapper.toModel(dto);
            User saved = userService.register(user);

            if (avatarFile != null && !avatarFile.isEmpty()) {
                userService.uploadAvatar(saved.getId(), avatarFile, saved.getId());
            }
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("generalError", e.getMessage());
            return "auth/register";
        }

        return "redirect:/pages/auth/login";
    }
}