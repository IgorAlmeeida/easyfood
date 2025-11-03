package br.com.imsa.easyfood.exception;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(NegocioException ex,
                                                                 HttpServletRequest request) {
        String error = messageSource.getMessage("error.bad.request", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(error)
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            if (errorMessage.contains("{")) {
                errors.put(fieldName, messageSource.getMessage(errorMessage.replace("{", "").replace("}", ""), null, LocaleContextHolder.getLocale()));
            } else {
                errors.put(fieldName, errorMessage);
            }
        });

        String error = messageSource.getMessage("validation.error", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("validation.failed", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(error)
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        errorResponse.addValidationErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.bad.request", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(error)
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.forbidden", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("access.denied", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .title(error)
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.entity.not.found", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title(error)
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.not.found", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("resource.notfound", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title(error)
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.method.not.allowed", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .title(error)
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.unsupported.media.type", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .title(error)
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.bad.request", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(error)
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.bad.request", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("error.type.mismatch",
                new Object[]{ex.getName(), ex.getRequiredType().getSimpleName()},
                LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(error)
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.bad.request", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("error.malformed.json", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(error)
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.conflict", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("error.database.constraint", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .title(error)
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        Map<String, String> extractFieldFromException = extractFieldFromException(ex);

        errorResponse.addValidationErrors(extractFieldFromException);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    private Map<String, String> extractFieldFromException(DataIntegrityViolationException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        Throwable root = NestedExceptionUtils.getMostSpecificCause(ex);
        if (root == null) {
            return fieldErrors;
        }

        String msg = root.getMessage();
        if (msg == null) {
            return fieldErrors;
        }

        Matcher keyMatcher = Pattern
                .compile("Key\\s*\\(([^)]+)\\)\\s*=\\s*\\(([^)]+)\\)", Pattern.CASE_INSENSITIVE)
                .matcher(msg);

        if (keyMatcher.find()) {
            String fields = keyMatcher.group(1).trim();
            String values = keyMatcher.group(2).trim();

            String[] fieldArray = fields.split("\\s*,\\s*");
            String[] valueArray = values.split("\\s*,\\s*");

            for (int i = 0; i < fieldArray.length; i++) {
                String field = fieldArray[i];
                String value = (i < valueArray.length) ? valueArray[i] : null;
                String message = "Valor já existente" + (value != null ? " (" + value + ")" : "");
                fieldErrors.put(field, message);
            }
        } else {
            Matcher constraintMatcher = Pattern
                    .compile("constraint\\s+\"([^\"]+)\"", Pattern.CASE_INSENSITIVE)
                    .matcher(msg);

            if (constraintMatcher.find()) {
                String constraint = constraintMatcher.group(1);
                fieldErrors.put("constraint", "Violação de integridade (" + constraint + ")");
            }
        }

        return fieldErrors;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.bad.request", null, LocaleContextHolder.getLocale());
        String message = ex.getMessage();

        if (message != null && message.startsWith("validation.")) {
            message = messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title(error)
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralExceptions(
            Exception ex,
            HttpServletRequest request) {

        String error = messageSource.getMessage("error.internal.server", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("error.unexpected", null, LocaleContextHolder.getLocale());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title(error)
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
