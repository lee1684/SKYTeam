package kr.co.ssalon.web.controller.exadvice;

import kr.co.ssalon.web.controller.PaymentController;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice(assignableTypes = PaymentController.class)
public class ExPaymentControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> BadRequestExHandle(BadRequestException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> HttpClientErrorExHandle(HttpClientErrorException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }
}
