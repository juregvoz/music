package com.music.handler;

import com.music.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.ThrowableProblem;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ThrowableProblem.class)
  public ResponseEntity<Error> handleThrowableProblem(ThrowableProblem problem) {
    Error dto = new Error();
    dto.setMessage(problem.getMessage());
    dto.setTime(new Date().toString());
    return new ResponseEntity<>(dto, HttpStatus.valueOf(problem.getStatus().getStatusCode()));
  }
}
