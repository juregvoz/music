package com.music.handler;

import com.music.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.DefaultProblem;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DefaultProblem.class)
  public ResponseEntity<Error> handleDefaultProblem(DefaultProblem problem) {
    Error dto = new Error();
    dto.setMessage(problem.getMessage());
    dto.setTime(new Date().toString());
    return new ResponseEntity<>(dto, HttpStatus.valueOf(problem.getStatus().getStatusCode()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Error> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    List<Map<String, String>> errors =
        exception.getFieldErrors().stream()
            .map(m -> Map.of(m.getField(), m.getDefaultMessage()))
            .sorted(Comparator.comparing(m -> m.entrySet().iterator().next().getKey()))
            .toList();

    Error dto = new Error();
    dto.setMessage(errors.toString());
    dto.setTime(new Date().toString());
    return new ResponseEntity<>(dto, exception.getStatusCode());
  }
}
