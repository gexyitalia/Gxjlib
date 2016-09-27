package com.gexy.auth.excepion;

public class AuthUsernameWrongSyntaxException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public AuthUsernameWrongSyntaxException () {
		super("Username contain illegal chars");
    }

    public AuthUsernameWrongSyntaxException (String _message) {
        super (_message);
    }

    public AuthUsernameWrongSyntaxException (Throwable _cause) {
        super (_cause);
    }

    public AuthUsernameWrongSyntaxException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public AuthUsernameWrongSyntaxException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
