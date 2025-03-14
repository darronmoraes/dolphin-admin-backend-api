package in.nineteen96.dolphin.controller.advisor;

import in.nineteen96.dolphin.exception.UserAlreadyExistsException;
import in.nineteen96.dolphin.exception.UserNotFoundException;
import in.nineteen96.dolphin.service.dto.model.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalAdvisor {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("invoked handlerHttpMessageNotReadableException \n{}", e.getMessage());
        ErrorDTO errorResponse = new ErrorDTO();
        errorResponse.setException("Http Message Not Readable Exception");
        errorResponse.setErrorMessage("request body is missing");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setSuccess(false);

        if (e.getMessage().contains("Unrecognized field")) {
            errorResponse.setErrorMessage(e.getMessage());
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDTO> handlerUserNotFoundException(UserNotFoundException e) {
        log.error("invoked handlerUserNotFoundException \n{}", e.getMessage());
        ErrorDTO errorResponse = new ErrorDTO();
        errorResponse.setException("User Not Found Exception");
        errorResponse.setErrorMessage(e.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setSuccess(false);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handlerUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error("invoked handlerUserAlreadyExistsException \n{}", e.getMessage());
        ErrorDTO errorResponse = new ErrorDTO();
        errorResponse.setException("User Already Exists Exception");
        errorResponse.setErrorMessage(e.getMessage());
        errorResponse.setStatus(HttpStatus.CONFLICT);
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setSuccess(false);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handlerException(Exception e) {
        log.error("invoked handlerException \n{}", e.getMessage());
        ErrorDTO errorResponse = new ErrorDTO();
        errorResponse.setException("Exception");
        errorResponse.setErrorMessage("Oops! something went wrong..");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setSuccess(false);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
