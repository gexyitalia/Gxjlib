package com.gexy.cli.exception;

public class CLIOptionArgumentTooManyPassedException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public CLIOptionArgumentTooManyPassedException () {

    }

    public CLIOptionArgumentTooManyPassedException (String _message) {
        super (_message);
    }

    public CLIOptionArgumentTooManyPassedException (Throwable _cause) {
        super (_cause);
    }

    public CLIOptionArgumentTooManyPassedException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public CLIOptionArgumentTooManyPassedException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
