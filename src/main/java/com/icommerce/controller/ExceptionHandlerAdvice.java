package com.icommerce.controller;


import com.icommerce.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
@Slf4j
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected ErrorResponse handleBadRequest (BadRequestException ex) {
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.NOT_FOUND, ex.getMessage(), ex.getData());
  }

  @ExceptionHandler(ProductIsNotEnoughException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected ErrorResponse handleNotEnoughProductsInStockException (ProductIsNotEnoughException ex) {
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.NOT_ENOUGH_QUANTITY, ex.getMessage(), null);
  }

  @ExceptionHandler(NotPermissionException.class)
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  protected ErrorResponse handleForbiddenException (NotPermissionException ex) {
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.FORBIDDEN, ex.getMessage(), null);
  }

  @ExceptionHandler(DataPersitException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  protected ErrorResponse handleDataPersitException (DataPersitException ex) {
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.DATA_PERSIT_VIOLATION, ex.getMessage(), null);
  }

}
