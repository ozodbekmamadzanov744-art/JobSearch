package kg.attractor.jobsearch.controller;

import org.springframework.http.HttpStatus;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.context.i18n.LocaleContextHolder;

@Controller
public class ErrorPageController {

    private final MessageSource messageSource;

    public ErrorPageController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/errors/403")
    public String forbidden(Model model) {
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("reason", HttpStatus.FORBIDDEN.getReasonPhrase());
        model.addAttribute("message", messageSource.getMessage(
                "error.forbidden",
                null,
                LocaleContextHolder.getLocale()));
        return "errors/error";
    }
}
