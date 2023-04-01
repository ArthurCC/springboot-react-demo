package fr.camposcosta.springbootreactdemo.controller;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import fr.camposcosta.springbootreactdemo.exception.EmailAlreadyExistsException;
import fr.camposcosta.springbootreactdemo.model.Response;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

        @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<Response<Void>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                ex, request, HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        @ExceptionHandler(NoHandlerFoundException.class)
        public ResponseEntity<Response<Void>> handleNoHandlerFoundException(NoHandlerFoundException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                ex, request, HttpStatus.NOT_FOUND, "Endpoint does not exist");
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Response<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                String fieldErrorMessages = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .distinct()
                                .map(err -> String.format("%s [value=%s] %s", err.getField(),
                                                err.getRejectedValue(), err.getDefaultMessage()))
                                .collect(Collectors.joining(","));

                return buildErrorResponse(
                                ex, request, HttpStatus.BAD_REQUEST, fieldErrorMessages);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Response<Void>> handleInvalidFormatException(HttpMessageNotReadableException ex,
                        HttpServletRequest request) {

                InvalidFormatException cause = (InvalidFormatException) ex.getCause();

                String errorMessage = String.format("[value=%s] is not a valid %s", cause.getValue(),
                                cause.getTargetType().getSimpleName());

                return buildErrorResponse(
                                ex, request, HttpStatus.BAD_REQUEST, errorMessage);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<Response<Void>> handleMethodArgumentTypeMismatchException(
                        MethodArgumentTypeMismatchException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                ex,
                                request,
                                HttpStatus.BAD_REQUEST,
                                String.format(
                                                "Invalid parameter format [parameter=%s] [requiredType=%s]",
                                                ex.getParameter().getParameterName(),
                                                ex.getRequiredType().getSimpleName()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Response<Void>> handleException(Exception ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

        private ResponseEntity<Response<Void>> buildErrorResponse(Exception ex,
                        HttpServletRequest request, HttpStatus httpStatus, String customMessage) {
                String errorMessage = String.format(
                                "%s [method=%s][path=%s] : %s",
                                customMessage,
                                request.getMethod(),
                                request.getRequestURI(),
                                ex.getMessage());

                log.error(errorMessage, ex);

                return new ResponseEntity<>(
                                new Response<>(LocalDateTime.now(), httpStatus, customMessage),
                                httpStatus);
        }
}
