package org.opentree.nexson.io;

public class NexsonParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String errorMessage = "Encounted a problem while attempting to parse NexSON";
	
	public NexsonParseException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return errorMessage;		
	}

}
