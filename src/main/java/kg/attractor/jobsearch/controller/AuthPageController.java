package kg.attractor.jobsearch.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.UserRegistrationDto;
import kg.attractor.jobsearch.exception.EmailAlreadyExistsException;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.mapper.UserMapper;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.service.PasswordResetService;
import kg.attractor.jobsearch.service.UserService;
import kg.attractor.jobsearch.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

import java.io.UnsupportedEncodingException;

@Slf4j
@Controller
@RequestMapping("/pages/auth")
public class AuthPageController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetService passwordResetService;
    private final MessageSource messageSource;

    public AuthPageController(UserService userService, AuthenticationManager authenticationManager,
                              PasswordResetService passwordResetService, MessageSource messageSource) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordResetService = passwordResetService;
        this.messageSource = messageSource;
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
            model.addAttribute("generalError", resolve("error.registration.emailExists"));
            return "auth/register";
        }

        authenticateAndCreateSession(saved.getEmail(), dto.getPassword(), request, response);

        boolean isEmployer = "EMPLOYER".equals(saved.getAccountType());
        return "redirect:" + (isEmployer ? "/pages/resumes" : "/pages/vacancies");
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "auth/forgot_password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, HttpServletRequest request, Model model) {
        try {
            passwordResetService.createResetToken(email, Utility.getSiteURL(request));
            model.addAttribute("message", resolve("forgot.success"));
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", resolve("forgot.userNotFound"));
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send password recovery email to address {}", email, e);
            model.addAttribute("error", resolve("forgot.mailFailed"));
        }
        return "auth/forgot_password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam String token, Model model) {
        try {
            passwordResetService.getByResetPasswordToken(token);
            model.addAttribute("token", token);
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", resolve("error.reset.invalidLink"));
        }
        return "auth/reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        try {
            User user = passwordResetService.getByResetPasswordToken(token);
            passwordResetService.resetPassword(user, password);
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", resolve("error.reset.invalidLink"));
            return "auth/reset_password";
        }
        return "redirect:/pages/auth/login?reset=success";
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

    private String resolve(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
