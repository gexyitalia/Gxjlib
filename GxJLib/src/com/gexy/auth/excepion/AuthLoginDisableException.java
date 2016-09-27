package com.gexy.auth.excepion;

public class AuthLoginDisableException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public AuthLoginDisableException () {
		super("Login failed, user is disable");
    }

    public AuthLoginDisableException (String _message) {
        super (_message);
    }

    public AuthLoginDisableException (Throwable _cause) {
        super (_cause);
    }

    public AuthLoginDisableException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public AuthLoginDisableException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
