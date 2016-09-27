package com.gexy.auth.excepion;

public class OrganizationRegisterUserException extends Exception {
	private static final long serialVersionUID = -844740799363332773L;
		public OrganizationRegisterUserException () {
			super("User exist");
	    }

	    public OrganizationRegisterUserException (String _message) {
	        super (_message);
	    }

	    public OrganizationRegisterUserException (Throwable _cause) {
	        super (_cause);
	    }

	    public OrganizationRegisterUserException (String _message, Throwable _cause) {
	        super (_message, _cause);
	    }
	    public OrganizationRegisterUserException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
	    			super(_message, _cause, _enableSuppression, _writableStackTrace);
	    		}

}
