package kg.attractor.jobsearch.exception;

import jakarta.servlet.http.HttpServletRequest;
import kg.attractor.jobsearch.controller.AuthController;
import kg.attractor.jobsearch.controller.RespondedApplicantController;
import kg.attractor.jobsearch.controller.ResumeController;
import kg.attractor.jobsearch.controller.UserController;
import kg.attractor.jobsearch.controller.VacancyController;
import kg.attractor.jobsearch.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Обрабатывает исключения, возникающие в REST-контроллерах, и всегда возвращает JSON
 * ({@link ErrorResponseDto}) с корректным HTTP-статусом, вместо HTML-страницы ошибки.
 */
@Slf4j
@RestControllerAdvice(assignableTypes = {
        AuthController.class,
        VacancyController.class,
        ResumeController.class,
        UserController.class,
        RespondedApplicantController.class
})
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null, request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null, request);
    }

    @ExceptionHandler(DuplicateResponseException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateResponse(DuplicateResponseException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), null, request);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleForbidden(ForbiddenOperationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage(), null, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage() == null ? "некорректное значение" : error.getDefaultMessage(),
                        (first, second) -> first,
                        LinkedHashMap::new));
        return buildResponse(HttpStatus.BAD_REQUEST, "Ошибка валидации входных данных", fieldErrors, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Параметр '" + ex.getName() + "' имеет некорректный формат";
        return buildResponse(HttpStatus.BAD_REQUEST, message, null, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Необработанное исключение при обработке REST-запроса {}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", null, request);
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(HttpStatus status, String message,
                                                            Map<String, String> fieldErrors, HttpServletRequest request) {
        ErrorResponseDto body = new ErrorResponseDto(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                fieldErrors
        );
        log.warn("Ошибка {} при обработке {} {}: {}", status.value(), request.getMethod(), request.getRequestURI(), message);
        return ResponseEntity.status(status).body(body);
    }
}
