package com.bol.game.kalaha.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bol.game.kalaha.exception.GameNotFoundException;
import com.bol.game.kalaha.exception.KalahaGameException;

@RestControllerAdvice
public class GameExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { GameNotFoundException.class })
	public ResponseEntity<Object> gameNotFound(Exception ex, WebRequest request) {
		String bodyOfResponse = "Not found!";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}
	
	@ExceptionHandler(value = { KalahaGameException.class })
	public ResponseEntity<Object> gameException(Exception ex, WebRequest request) {
		String bodyOfResponse = "Error Occured : " + ex.getMessage();
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);

	}

}
