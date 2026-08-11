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
        return "profile/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("profileDto") UserProfileUpdateDto dto,
                       BindingResult bindingResult,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountType", userDetails.getUser().getAccountType());
            return "profile/edit";
        }

        Long userId = userDetails.getUser().getId();
        User updates = UserMapper.toModel(dto);
        userService.updateProfile(userId, updates, userId);

        return "redirect:/pages/cabinet";
    }
}