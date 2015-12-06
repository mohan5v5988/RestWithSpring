package org.moham.app.exception;

public class PKException extends GException {
	
	public PKException() {
		super();
	}
	
	public PKException(String message){
		super(message);
	}
	
	public PKException(Throwable cause) {
		super(cause);
	}
	
	public PKException(String message, Throwable cause) {
		super(message, cause);
	}
}
