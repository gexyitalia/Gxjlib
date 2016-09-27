package com.gexy.config.exception;

public class ConfigFileContextNotFound extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public ConfigFileContextNotFound () {

    }

    public ConfigFileContextNotFound (String _message) {
        super (_message);
    }

    public ConfigFileContextNotFound (Throwable _cause) {
        super (_cause);
    }

    public ConfigFileContextNotFound (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public ConfigFileContextNotFound(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
