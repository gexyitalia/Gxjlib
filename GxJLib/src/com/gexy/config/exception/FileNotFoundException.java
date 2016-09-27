package com.gexy.config.exception;

public class FileNotFoundException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public FileNotFoundException () {

    }

    public FileNotFoundException (String _message) {
        super (_message);
    }

    public FileNotFoundException (Throwable _cause) {
        super (_cause);
    }

    public FileNotFoundException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public FileNotFoundException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
