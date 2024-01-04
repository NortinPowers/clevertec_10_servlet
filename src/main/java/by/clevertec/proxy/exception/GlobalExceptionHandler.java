package by.clevertec.proxy.exception;

import static by.clevertec.proxy.util.LogUtil.getErrorMessageToLog;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleError(Exception exception, HttpServletRequest request) {
        log.error(getErrorMessageToLog("handleError()", GlobalExceptionHandler.class), exception);
        return new ResponseEntity<>("Some error occurred (" + request.getRequestURL() + ")", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
