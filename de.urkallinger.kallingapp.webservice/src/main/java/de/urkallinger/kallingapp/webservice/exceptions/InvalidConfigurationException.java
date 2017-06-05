package de.urkallinger.kallingapp.webservice.exceptions;

public class InvalidConfigurationException extends Exception {

	private static final long serialVersionUID = 1650769069779993423L;
	
    public InvalidConfigurationException() {
        super();
    }
    public InvalidConfigurationException(String message) {
        super(message);
    }
    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }
    protected InvalidConfigurationException(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
