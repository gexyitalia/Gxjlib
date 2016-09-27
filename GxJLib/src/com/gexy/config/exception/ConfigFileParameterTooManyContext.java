package com.gexy.config.exception;

public class ConfigFileParameterTooManyContext extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public ConfigFileParameterTooManyContext () {

    }

    public ConfigFileParameterTooManyContext (String _message) {
        super (_message);
    }

    public ConfigFileParameterTooManyContext (Throwable _cause) {
        super (_cause);
    }

    public ConfigFileParameterTooManyContext (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public ConfigFileParameterTooManyContext(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
