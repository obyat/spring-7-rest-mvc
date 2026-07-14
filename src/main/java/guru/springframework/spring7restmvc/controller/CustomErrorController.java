package guru.springframework.spring7restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler
    ResponseEntity<List<Map<String, String>>> handleJPAViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException constraintViolationException) {
            List<Map<String, String>> errorList = constraintViolationException.getConstraintViolations().stream()
                    .map(cve -> {
                        Map<String, String> errors = new HashMap<>();
                        errors.put(cve.getPropertyPath().toString(), cve.getMessage());
                        return errors;
                    }).collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorList);
        }
        return ResponseEntity.badRequest().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBingErrors(MethodArgumentNotValidException exception) {

        List<Map<String, String>> errorList = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errors = new HashMap<>();
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errors;
                }).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errorList);
    }
}
