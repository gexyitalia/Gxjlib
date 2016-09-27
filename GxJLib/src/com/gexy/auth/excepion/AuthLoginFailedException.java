package com.gexy.auth.excepion;

public class AuthLoginFailedException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public AuthLoginFailedException () {
		super("Login failed, wrong credentials");
    }

    public AuthLoginFailedException (String _message) {
        super (_message);
    }

    public AuthLoginFailedException (Throwable _cause) {
        super (_cause);
    }

    public AuthLoginFailedException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public AuthLoginFailedException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
