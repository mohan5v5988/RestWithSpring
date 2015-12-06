package org.moham.app.exception;

public class NotFoundException extends GException {
	
	private static final long serialVersionUID = 1L;
	
	Object obj;
	public NotFoundException() {
		super();
	}
	
	public NotFoundException(String message, Object object) {
		super(message);
		this.obj = object;
	}
	
	@Override
	public String getMessage() {
		String m = super.getMessage();
		return m + "The Object is { " + obj.toString() + " }";
	}
}
