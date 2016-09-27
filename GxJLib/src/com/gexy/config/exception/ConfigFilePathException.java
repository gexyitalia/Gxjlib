package com.gexy.config.exception;

public class ConfigFilePathException extends Exception {
	private static final long serialVersionUID = -5564242739966677354L;
	public ConfigFilePathException () {

    }

    public ConfigFilePathException (String _message) {
        super (_message);
    }

    public ConfigFilePathException (Throwable _cause) {
        super (_cause);
    }

    public ConfigFilePathException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public ConfigFilePathException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
