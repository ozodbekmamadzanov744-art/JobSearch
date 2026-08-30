package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.UserProfileUpdateDto;
import kg.attractor.jobsearch.mapper.UserMapper;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/pages/profile")
public class ProfilePageController {

    private final UserService userService;

    public ProfilePageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit")
    public String editForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User current = userDetails.getUser();

        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        dto.setName(current.getName());
        dto.setSurname(current.getSurname());
        dto.setAge(current.getAge());
        dto.setPhoneNumber(current.getPhoneNumber());

        model.addAttribute("profileDto", dto);
        model.addAttribute("accountType", current.getAccountType());
        model.addAttribute("user", current);
        return "profile/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("profileDto") UserProfileUpdateDto dto,
                       BindingResult bindingResult,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        User currentUser = userDetails.getUser();
        if ("APPLICANT".equals(currentUser.getAccountType())) {
            if (dto.getSurname() == null || dto.getSurname().isBlank()) {
                bindingResult.rejectValue("surname", "surname.required", "Фамилия обязательна для заполнения");
            }
            if (dto.getAge() == null) {
                bindingResult.rejectValue("age", "age.required", "Возраст обязателен для заполнения");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("accountType", currentUser.getAccountType());
            model.addAttribute("user", currentUser);
            return "profile/edit";
        }

        Long userId = currentUser.getId();
        User updates = UserMapper.toModel(dto);
        userService.updateProfile(userId, updates, userId);

        return "redirect:/pages/cabinet";
    }

    @PostMapping("/avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        userService.uploadAvatar(userId, file, userId);
        return "redirect:/pages/profile/edit";
    }
}
