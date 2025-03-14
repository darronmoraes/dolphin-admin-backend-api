package in.nineteen96.dolphin.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.nineteen96.dolphin.exception.AuthorizationHeaderException;
import in.nineteen96.dolphin.service.dto.model.ErrorDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.Instant;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("authentication failed", authException);

        ErrorDTO errorDTO = new ErrorDTO();

        if (request.getAttribute("authorization-header") != null) {
            logger.warn("handling authorization header not found");
            AuthorizationHeaderException e = (AuthorizationHeaderException) request.getAttribute("authorization-header");

            errorDTO.setErrorMessage(e.getMessage());
            errorDTO.setException("Authorization Header Exception");
        } else if (request.getAttribute("expired-token") != null) {
            logger.warn("handling expired jwt token");
            ExpiredJwtException e = (ExpiredJwtException) request.getAttribute("expired-token");

            errorDTO.setErrorMessage(e.getMessage());
            errorDTO.setException("Expired Jwt Exception");
        } else {
            logger.warn("handling other token exception");
            errorDTO.setErrorMessage(authException.getMessage());
            errorDTO.setException("Token Verification Exception");
        }

        errorDTO.setStatus(HttpStatus.UNAUTHORIZED);
        errorDTO.setSuccess(false);
        errorDTO.setTimestamp(Instant.now());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        OutputStream os = response.getOutputStream();
        objectMapper.writeValue(os, errorDTO);
        os.flush();
    }
}
