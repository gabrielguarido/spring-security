package com.springsecurity.base.exception.handler;

import com.springsecurity.base.exception.ResourceNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Treat all exceptions from the Controller layer.
 *
 * @author Gabriel Oliveira
 */
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String userMessage = messageSource.getMessage("message.invalid", null, LocaleContextHolder.getLocale());
        String completeMessage = ex.getCause() != null ? ex.getCause().toString() : ex.toString();

        List<Error> errorList = Collections.singletonList(new Error(userMessage, completeMessage));

        return handleExceptionInternal(ex, errorList, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Error> errorList = createErrorList(ex.getBindingResult());

        return handleExceptionInternal(ex, errorList, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String userMessage = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
        String completeMessage = ex.toString();
        List<Error> errorList = Collections.singletonList(new Error(userMessage, completeMessage));

        return handleExceptionInternal(ex, errorList, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String userMessage = messageSource.getMessage("resource.operation-not-allowed", null, LocaleContextHolder.getLocale());
        String completeMessage = ExceptionUtils.getRootCauseMessage(ex);
        List<Error> errorList = Collections.singletonList(new Error(userMessage, completeMessage));

        return handleExceptionInternal(ex, errorList, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        String userMessage = messageSource.getMessage("error.unexpected", null, LocaleContextHolder.getLocale());
        String completeMessage = ex.getMessage();
        List<Error> errorList = Collections.singletonList(new Error(userMessage, completeMessage));

        return new ResponseEntity<>(errorList, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(Exception ex) {
        String userMessage = ex.getMessage();
        String completeMessage = ex.getMessage();
        List<Error> errorList = Collections.singletonList(new Error(userMessage, completeMessage));

        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

    private List<Error> createErrorList(BindingResult bindingResult) {
        List<Error> errorList = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            String userMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String completeMessage = fieldError.toString();
            errorList.add(new Error(userMessage, completeMessage));
        });

        return errorList;
    }

    public static class Error {

        private final String userMessage;
        private final String completeMessage;

        public Error(String userMessage, String completeMessage) {
            this.userMessage = userMessage;
            this.completeMessage = completeMessage;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public String getCompleteMessage() {
            return completeMessage;
        }

    }

}
