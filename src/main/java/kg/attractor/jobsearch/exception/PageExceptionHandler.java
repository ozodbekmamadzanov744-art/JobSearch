package kg.attractor.jobsearch.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.attractor.jobsearch.controller.AuthPageController;
import kg.attractor.jobsearch.controller.CabinetController;
import kg.attractor.jobsearch.controller.CompanyPageController;
import kg.attractor.jobsearch.controller.ErrorPageController;
import kg.attractor.jobsearch.controller.ProfilePageController;
import kg.attractor.jobsearch.controller.ResumePageController;
import kg.attractor.jobsearch.controller.VacancyPageController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


@Slf4j
@ControllerAdvice(assignableTypes = {
        AuthPageController.class,
        CabinetController.class,
        CompanyPageController.class,
        ErrorPageController.class,
        ProfilePageController.class,
        ResumePageController.class,
        VacancyPageController.class
})
public class PageExceptionHandler {

    private final MessageSource messageSource;

    public PageExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, HttpServletRequest request,
                                 HttpServletResponse response, Model model) {
        return buildErrorPage(HttpStatus.NOT_FOUND, resolveError(ex.getMessage()), request, response, model);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest request,
                                    HttpServletResponse response, Model model) {
        return buildErrorPage(HttpStatus.CONFLICT, resolveError(ex.getMessage()), request, response, model);
    }

    @ExceptionHandler(DuplicateResponseException.class)
    public String handleDuplicateResponse(DuplicateResponseException ex, HttpServletRequest request,
                                          HttpServletResponse response, Model model) {
        return buildErrorPage(HttpStatus.CONFLICT, resolveError(ex.getMessage()), request, response, model);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public String handleForbidden(ForbiddenOperationException ex, HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        return buildErrorPage(HttpStatus.FORBIDDEN, resolveError(ex.getMessage()), request, response, model);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request,
                                   HttpServletResponse response, Model model) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + resolveMessage(error.getDefaultMessage()))
                .reduce((first, second) -> first + "; " + second)
                .orElse(resolve("error.validation"));
        return buildErrorPage(HttpStatus.BAD_REQUEST, message, request, response, model);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request,
                                     HttpServletResponse response, Model model) {
        String message = resolve("error.badRequest.param", ex.getName());
        return buildErrorPage(HttpStatus.BAD_REQUEST, message, request, response, model);
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, HttpServletRequest request,
                                HttpServletResponse response, Model model) {
        log.error("Необработанное исключение при обработке запроса {}", request.getRequestURI(), ex);
        return buildErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, resolve("error.server"), request, response, model);
    }

    private String buildErrorPage(HttpStatus status, String message, HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        response.setStatus(status.value());
        model.addAttribute("status", status.value());
        model.addAttribute("reason", status.getReasonPhrase());
        model.addAttribute("message", message);
        model.addAttribute("details", request);
        return "errors/error";
    }

    private String resolve(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    private String resolveError(String message) {
        if (message == null) {
            return resolve("error.server");
        }
        if (message.startsWith("error.") || message.startsWith("validation.")) {
            return resolve(message);
        }
        if (message.startsWith("{") && message.endsWith("}")) {
            return resolve(message.substring(1, message.length() - 1));
        }
        return message;
    }

    private String resolveMessage(String defaultMessage) {
        if (defaultMessage == null) {
            return resolve("error.validation.field");
        }
        if (defaultMessage.startsWith("{") && defaultMessage.endsWith("}")) {
            return resolve(defaultMessage.substring(1, defaultMessage.length() - 1));
        }
        return messageSource.getMessage(defaultMessage, null, defaultMessage, LocaleContextHolder.getLocale());
    }
}
