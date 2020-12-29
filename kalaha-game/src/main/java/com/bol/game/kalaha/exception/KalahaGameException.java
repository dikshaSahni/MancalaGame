package com.bol.game.kalaha.exception;

import org.springframework.stereotype.Component;

@Component
public class KalahaGameException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public KalahaGameException(String message) {
        super(message);
    }
	
	public KalahaGameException() {
        super();
    }
}
