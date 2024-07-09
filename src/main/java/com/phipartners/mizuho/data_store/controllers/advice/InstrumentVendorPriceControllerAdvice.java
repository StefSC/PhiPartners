package com.phipartners.mizuho.data_store.controllers.advice;

import com.phipartners.mizuho.data_store.model.dto.ErrorData;
import com.phipartners.mizuho.data_store.model.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class InstrumentVendorPriceControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorData> notFoundResponse(NotFoundException exception) {
        log.error("Not found", exception);
        return new ResponseEntity<>(ErrorData.builder().info(exception.getMessage()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorData> validationErrorResponse(MethodArgumentNotValidException exception) {
        log.error("Invalid data", exception);
        return new ResponseEntity<>(ErrorData.builder().info(exception.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorData> errorResponse(Exception exception) {
        log.error("ERROR", exception);
        return new ResponseEntity<>(ErrorData.builder().info(exception.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
