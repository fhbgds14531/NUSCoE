package nuscoe.prog.fhbgds.util;

public class InvalidNameException extends Throwable {

	private static final long serialVersionUID = -8993120160420336183L;
	private String message;
	
	
	public InvalidNameException(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
