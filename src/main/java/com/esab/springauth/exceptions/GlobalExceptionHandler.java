package com.esab.springauth.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public String handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException ex) {
    return "{\"errors\": [\"Access Denied: You do not have the required role\"]}";
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public String handleAuthenticationException(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException ex) {
    return "{\"error\": [\"Authentication required: Token not provided or invalid\"]}";
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(Exception ex) {
    List<String> errors = List.of(ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorsMap(errors));
  }

  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<Map<String, List<String>>> handleRuntimeExceptions(RuntimeException ex) {
    List<String> errors = List.of(ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorsMap(errors));
  }

  @ExceptionHandler(InvalidJwtException.class)
  public ResponseEntity<Map<String, List<String>>> handleJwtErrors(InvalidJwtException ex) {

    List<String> errors = List.of(ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsMap(errors));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, List<String>>> handleBadCredentialsError(BadCredentialsException ex) {

    List<String> errors = List.of(ex.getMessage());

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorsMap(errors));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  private Map<String, List<String>> errorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }

}