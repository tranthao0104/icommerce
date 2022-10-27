package com.icommerce.controller;

import com.icommerce.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerAdviceTests {

  @InjectMocks
  private ExceptionHandlerAdvice exceptionHandler;

  @Test
  void handleBadRequest() {
    ErrorResponse actual = exceptionHandler
        .handleBadRequest(new BadRequestException("msg", null));

    // THEN
    Assertions.assertEquals(StatusCode.NOT_FOUND, actual.getStatusCode());
  }

  @Test
  void handleNotEnoughProductsInStock() {
    ErrorResponse actual = exceptionHandler.handleNotEnoughProductsInStockException(
            new ProductIsNotEnoughException("msg"));

    // THEN
    Assertions.assertEquals(StatusCode.NOT_ENOUGH_QUANTITY, actual.getStatusCode());
  }

  @Test
  void handleDataPersitException() {
    ErrorResponse actual = exceptionHandler.handleDataPersitException(
            new DataPersitException("msg"));

    // THEN
    Assertions.assertEquals(StatusCode.DATA_PERSIT_VIOLATION, actual.getStatusCode());
  }

  @Test
  void handleForbiddenException() {
    ErrorResponse actual = exceptionHandler.handleForbiddenException(
            new NotPermissionException("msg"));

    // THEN
    Assertions.assertEquals(StatusCode.FORBIDDEN, actual.getStatusCode());
  }
}