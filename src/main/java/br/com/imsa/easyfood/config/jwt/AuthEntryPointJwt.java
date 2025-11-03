package br.com.imsa.easyfood.config.jwt;

import br.com.imsa.easyfood.exception.ErrorResponse;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private final MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        var locale = LocaleContextHolder.getLocale();

        String title = messageSource.getMessage("error.unauthorized", null, locale);
        String detail = messageSource.getMessage("auth.token.invalid", null, locale);

        LocalDateTime timestamp = LocalDateTime.now();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .title(title)
                .detail(detail)
                .instance(request.getRequestURI())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        mapper.writeValue(response.getOutputStream(), errorResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}

