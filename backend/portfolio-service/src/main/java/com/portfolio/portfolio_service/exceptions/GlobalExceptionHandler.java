package com.portfolio.portfolio_service.exceptions;

import com.portfolio.portfolio_service.projects.exceptions.DuplicateProjectException;
import com.portfolio.portfolio_service.projects.exceptions.InvalidProjectUpdateException;
import com.portfolio.portfolio_service.skill.exceptions.DuplicateSkillException;
import com.portfolio.portfolio_service.skill.exceptions.InvalidSkillUpdateException;
import com.portfolio.portfolio_service.skill.exceptions.SkillNotFoundException;
import com.portfolio.portfolio_service.testimonials.exceptions.DuplicateTestimonialException;
import com.portfolio.portfolio_service.testimonials.exceptions.InvalidTestimonialUpdateException;
import com.portfolio.portfolio_service.testimonials.exceptions.TestimonialNotFoundException;
import com.portfolio.portfolio_service.workexp.exceptions.DuplicateWorkExpException;
import com.portfolio.portfolio_service.workexp.exceptions.InvalidWorkExpUpdateException;
import com.portfolio.portfolio_service.workexp.exceptions.WorkExpNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingServletRequestPartException(
            MissingServletRequestPartException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Missing Request Part",
                String.format("Required part '%s' is missing", ex.getRequestPartName()),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getParameterValidationResults().forEach(result ->
                result.getResolvableErrors().forEach(error ->
                        errors.put(Objects.requireNonNull(error.getCodes())[0], error.getDefaultMessage())
                )
        );

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                errors.toString(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                errors.toString(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleSkillNotFoundException(
            SkillNotFoundException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Skill Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateSkillException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateSkillException(
            DuplicateSkillException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Duplicate Skill",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidSkillUpdateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSkillUpdateException(
            InvalidSkillUpdateException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Skill Update",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ApiErrorResponse> handleStorageException(
            StorageException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Storage Error",
                ex.getMessage() != null ? ex.getMessage() : "Failed to process storage operation",
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handleFileProcessingException(
            FileProcessingException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "File Upload Error",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(WorkExpNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleWorkExpNotFoundException(
            WorkExpNotFoundException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Work Experience Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateWorkExpException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateWorkExpException(
            DuplicateWorkExpException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Duplicate Work Experience",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidWorkExpUpdateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidWorkExpUpdateException(
            InvalidWorkExpUpdateException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Work Experience Update",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateProjectException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateProjectException(
            DuplicateProjectException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Duplicate Project",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidProjectUpdateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidProjectUpdateException(
            InvalidProjectUpdateException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Project Update",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateTestimonialException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateTestimonialException(
            DuplicateTestimonialException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Duplicate Testimonial",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidTestimonialUpdateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTestimonialUpdateException(
            InvalidTestimonialUpdateException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Testimonial Update",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(TestimonialNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTestimonialNotFoundException(
            TestimonialNotFoundException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Testimonial Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected Error",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
