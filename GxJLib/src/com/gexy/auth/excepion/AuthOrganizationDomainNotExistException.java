package com.gexy.auth.excepion;

public class AuthOrganizationDomainNotExistException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public AuthOrganizationDomainNotExistException () {
		super("Nothing organization founded for the specified domain");
    }

    public AuthOrganizationDomainNotExistException (String _message) {
        super (_message);
    }

    public AuthOrganizationDomainNotExistException (Throwable _cause) {
        super (_cause);
    }

    public AuthOrganizationDomainNotExistException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public AuthOrganizationDomainNotExistException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
