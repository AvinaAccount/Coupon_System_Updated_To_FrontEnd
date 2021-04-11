package com.Avinadav.couponsystem.rest.controller;

import com.Avinadav.couponsystem.rest.ex.*;
import com.Avinadav.couponsystem.rest.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorized(InvalidLoginException ex) {
        return ErrorResponse.ofNow(ex.getMessage());
    }

    @ExceptionHandler(PurchaseCouponException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse purchaseHandler(PurchaseCouponException ex) {
        return ErrorResponse.ofNow(ex.getMessage());
    }

    /*All the CRUD operations*/
    @ExceptionHandler({
            RemoveException.class,
            CreateException.class,
            UpdateException.class,
            FetchException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse createHandler(Exception ex) {
        return ErrorResponse.ofNow(ex.getMessage());
    }
}
