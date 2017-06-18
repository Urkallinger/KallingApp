package de.urkallinger.kallingapp.webservice.exceptions;

import java.io.Serializable;

public class UnauthorizedException extends Exception implements Serializable {

	private static final long serialVersionUID = 3927021065025534926L;

	   public UnauthorizedException() {
	        super();
	    }
	    public UnauthorizedException(String message) {
	        super(message);
	    }
	    public UnauthorizedException(String message, Throwable cause) {
	        super(message, cause);
	    }
	    public UnauthorizedException(Throwable cause) {
	        super(cause);
	    }
	    protected UnauthorizedException(String message, Throwable cause,
	                        boolean enableSuppression,
	                        boolean writableStackTrace) {
	        super(message, cause, enableSuppression, writableStackTrace);
	    }
}
