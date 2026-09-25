package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.model.Message;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.repository.MessageRepository;
import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.RespondedApplicantService;
import kg.attractor.jobsearch.service.ResumeService;
import kg.attractor.jobsearch.service.UserService;
import kg.attractor.jobsearch.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/pages")
@RequiredArgsConstructor
public class ChatPageController {

    private final RespondedApplicantService respondedApplicantService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final UserService userService;
    private final MessageRepository messageRepository;

    @GetMapping("/responses")
    public String myResponses(@AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        User user = userService.getUserById(userDetails.getUser().getId());

        if (!"APPLICANT".equals(user.getAccountType())) {
            throw new ForbiddenOperationException("error.chat.participant");
        }

        model.addAttribute("responses", respondedApplicantService.findByApplicantId(user.getId()));
        return "responses/list";
    }

    @GetMapping("/chats/{responseId}")
    public String chat(@PathVariable Long responseId,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        RespondedApplicant response = getAuthorizedResponse(responseId, userDetails.getUser().getId());
        model.addAttribute("response", response);
        model.addAttribute("messages", messageRepository.findByRespondedApplicantIdOrderByTimestampAsc(responseId));
        return "chats/detail";
    }

    @PostMapping("/chats/{responseId}/messages")
    public String sendMessage(@PathVariable Long responseId,
                              @RequestParam String content,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        RespondedApplicant response = getAuthorizedResponse(responseId, userDetails.getUser().getId());

        if (content == null || content.isBlank()) {
            redirectAttributes.addFlashAttribute("messageError", "validation.message.content");
            return "redirect:/pages/chats/" + responseId;
        }

        Message message = new Message();
        message.setRespondedApplicant(response);
        message.setSender(userReference(userDetails.getUser().getId()));
        message.setContent(content.trim());
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);

        return "redirect:/pages/chats/" + responseId;
    }

    @PostMapping("/chats/start")
    public String startByEmployer(@RequestParam Long resumeId,
                                  @RequestParam Long vacancyId,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        Resume resume = resumeService.getResumeById(resumeId);
        Vacancy vacancy = vacancyService.getVacancyById(vacancyId);

        if (vacancy.getAuthor() == null || !vacancy.getAuthor().getId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException("error.vacancy.owner");
        }

        if (!Boolean.TRUE.equals(vacancy.getIsActive())) {
            throw new ForbiddenOperationException("error.chat.activeVacancy");
        }

        RespondedApplicant response = new RespondedApplicant();
        response.setResume(resumeReference(resume.getId()));
        response.setVacancy(vacancyReference(vacancy.getId()));
        response.setConfirmation(false);

        RespondedApplicant saved = respondedApplicantService.findOrCreateResponse(response);
        return "redirect:/pages/chats/" + saved.getId();
    }

    private RespondedApplicant getAuthorizedResponse(Long responseId, Long userId) {
        RespondedApplicant response = respondedApplicantService.getById(responseId);

        Long applicantId = response.getResume() != null && response.getResume().getApplicant() != null
                ? response.getResume().getApplicant().getId()
                : null;
        Long employerId = response.getVacancy() != null && response.getVacancy().getAuthor() != null
                ? response.getVacancy().getAuthor().getId()
                : null;

        if (!userId.equals(applicantId) && !userId.equals(employerId)) {
            throw new ForbiddenOperationException("error.chat.participant");
        }

        return response;
    }

    private User userReference(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private Resume resumeReference(Long id) {
        Resume resume = new Resume();
        resume.setId(id);
        return resume;
    }

    private Vacancy vacancyReference(Long id) {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(id);
        return vacancy;
    }
}
