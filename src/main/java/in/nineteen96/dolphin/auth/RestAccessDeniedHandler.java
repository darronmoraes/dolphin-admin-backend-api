package in.nineteen96.dolphin.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.nineteen96.dolphin.service.dto.model.ErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setErrorMessage(accessDeniedException.getMessage());
        errorDTO.setException(accessDeniedException.toString());
        errorDTO.setStatus(HttpStatus.FORBIDDEN);
        errorDTO.setSuccess(false);
        errorDTO.setTimestamp(Instant.now());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        OutputStream os = response.getOutputStream();
        objectMapper.writeValue(os, errorDTO);
        os.flush();
    }
}
