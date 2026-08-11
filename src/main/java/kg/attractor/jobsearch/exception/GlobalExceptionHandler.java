package kg.attractor.jobsearch.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, HttpServletRequest request, Model model) {
        return buildErrorPage(HttpStatus.NOT_FOUND, ex.getMessage(), request, model);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest request, Model model) {
        return buildErrorPage(HttpStatus.CONFLICT, ex.getMessage(), request, model);
    }

    @ExceptionHandler(DuplicateResponseException.class)
    public String handleDuplicateResponse(DuplicateResponseException ex, HttpServletRequest request, Model model) {
        return buildErrorPage(HttpStatus.CONFLICT, ex.getMessage(), request, model);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public String handleForbidden(ForbiddenOperationException ex, HttpServletRequest request, Model model) {
        return buildErrorPage(HttpStatus.FORBIDDEN, ex.getMessage(), request, model);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request, Model model) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((first, second) -> first + "; " + second)
                .orElse("Ошибка валидации входных данных");
        return buildErrorPage(HttpStatus.BAD_REQUEST, message, request, model);
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, HttpServletRequest request, Model model) {
        return buildErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", request, model);
    }

    private String buildErrorPage(HttpStatus status, String message, HttpServletRequest request, Model model) {
        model.addAttribute("status", status.value());
        model.addAttribute("reason", status.getReasonPhrase());
        model.addAttribute("message", message);
        model.addAttribute("details", request);
        return "errors/error";
    }
}