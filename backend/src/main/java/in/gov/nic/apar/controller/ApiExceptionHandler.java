package in.gov.nic.apar.controller;

import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<Map<String,String>> badRequest(IllegalArgumentException e){return ResponseEntity.badRequest().body(Map.of("message",safe(e.getMessage())));}
 @ExceptionHandler(SecurityException.class) public ResponseEntity<Map<String,String>> forbidden(SecurityException e){return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message",safe(e.getMessage())));}
 @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class) public ResponseEntity<Map<String,String>> conflict(Exception e){return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","A record with the same employee code or email already exists."));}
 private String safe(String s){return s==null||s.isBlank()?"Request could not be completed":s;}
}
