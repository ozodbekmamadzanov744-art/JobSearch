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

/**
 * Обрабатывает исключения, возникающие в MVC (страничных) контроллерах, и всегда
 * рендерит HTML-страницу ошибки с корректно выставленным HTTP-статусом ответа.
 * Для REST-контроллеров исключения обрабатывает {@link RestExceptionHandler}.
 */
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, HttpServletRequest request,
                                 HttpServletResponse response, Model model) {
        return buildErrorPage(HttpStatus.NOT_FOUND, ex.getMessage(), request, response, model);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest request,
                                    HttpServletResponse response, Model model) {
        return buildErrorPage(HttpStatus.CONFLICT, ex.getMessage(), request, response, model);
    }

    @ExceptionHandler(DuplicateResponseException.class)
    public String handleDuplicateResponse(DuplicateResponseException ex, HttpServletRequest request,
                                          HttpServletResponse response, Model model) {
        return buildErrorPage(HttpStatus.CONFLICT, ex.getMessage(), request, response, model);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public String handleForbidden(ForbiddenOperationException ex, HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        return buildErrorPage(HttpStatus.FORBIDDEN, ex.getMessage(), request, response, model);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request,
                                   HttpServletResponse response, Model model) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((first, second) -> first + "; " + second)
                .orElse("Ошибка валидации входных данных");
        return buildErrorPage(HttpStatus.BAD_REQUEST, message, request, response, model);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request,
                                     HttpServletResponse response, Model model) {
        String message = "Параметр '" + ex.getName() + "' имеет некорректный формат";
        return buildErrorPage(HttpStatus.BAD_REQUEST, message, request, response, model);
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, HttpServletRequest request,
                                HttpServletResponse response, Model model) {
        log.error("Необработанное исключение при обработке запроса {}", request.getRequestURI(), ex);
        return buildErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", request, response, model);
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
}
