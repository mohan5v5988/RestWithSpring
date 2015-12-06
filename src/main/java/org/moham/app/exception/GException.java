package org.moham.app.exception;

public class GException extends Exception {
	
private static final long serialVersionUID = 1L;
	
	public GException() {
		super();
	}
	
	public GException(String message){
		super(message);
	}
	
	public GException(Throwable cause) {
		super(cause);
	}
	
	public GException(String message, Throwable cause) {
		super(message, cause);
	}
}
