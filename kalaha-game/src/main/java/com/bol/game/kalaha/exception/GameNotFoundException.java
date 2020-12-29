package com.bol.game.kalaha.exception;

public class GameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public GameNotFoundException(String message) {
        super(message);
    }
	
	public GameNotFoundException() {
        super();
    }

}
