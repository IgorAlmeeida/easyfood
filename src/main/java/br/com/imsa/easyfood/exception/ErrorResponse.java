package br.com.imsa.easyfood.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private List<ValidationError> errors;

    public void addValidationError(String field, String message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }

    public void addValidationErrors(Map<String, String> errors) {
        errors.forEach(this::addValidationError);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}