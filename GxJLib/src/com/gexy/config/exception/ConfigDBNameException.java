package com.gexy.config.exception;

public class ConfigDBNameException extends Exception {
	private static final long serialVersionUID = -5564242739966677354L;
	public ConfigDBNameException () {

    }

    public ConfigDBNameException (String _message) {
        super (_message);
    }

    public ConfigDBNameException (Throwable _cause) {
        super (_cause);
    }

    public ConfigDBNameException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public ConfigDBNameException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
