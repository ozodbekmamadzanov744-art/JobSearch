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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        AuthController.class,
        VacancyController.class,
        ResumeController.class,
        UserController.class,
        RespondedApplicantController.class
})
public class RestExceptionHandler {

    private final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, resolveError(ex.getMessage()), null, request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, resolveError(ex.getMessage()), null, request);
    }

    @ExceptionHandler(DuplicateResponseException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateResponse(DuplicateResponseException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, resolveError(ex.getMessage()), null, request);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleForbidden(ForbiddenOperationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, resolveError(ex.getMessage()), null, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return buildValidationResponse(ex.getBindingResult(), request);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException ex, HttpServletRequest request) {
        return buildValidationResponse(ex.getBindingResult(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = resolve("error.badRequest.param", ex.getName());
        return buildResponse(HttpStatus.BAD_REQUEST, message, null, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Необработанное исключение при обработке REST-запроса {}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, resolve("error.server"), null, request);
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

    private ResponseEntity<ErrorResponseDto> buildValidationResponse(BindingResult bindingResult,
                                                                     HttpServletRequest request) {
        Map<String, String> fieldErrors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        this::resolveFieldError,
                        (first, second) -> first,
                        LinkedHashMap::new));
        return buildResponse(HttpStatus.BAD_REQUEST, resolve("error.validation"), fieldErrors, request);
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

    private String resolveFieldError(FieldError error) {
        String defaultMessage = error.getDefaultMessage();

        if (defaultMessage != null) {
            return resolveError(defaultMessage);
        }

        String code = error.getCode();

        if (code != null) {
            return resolveError(code);
        }

        return resolve("error.validation.field");
    }
}
