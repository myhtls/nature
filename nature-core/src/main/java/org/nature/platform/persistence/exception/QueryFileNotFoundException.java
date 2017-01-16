package org.nature.platform.persistence.exception;

public class QueryFileNotFoundException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2613993389908896057L;

	public QueryFileNotFoundException(){
		
	}
	
	public QueryFileNotFoundException(String messages){
		super(messages);
	}

}
