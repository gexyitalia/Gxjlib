package com.gexy.cli.exception;

public class CLIOptionArgumentValueException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public CLIOptionArgumentValueException () {

    }

    public CLIOptionArgumentValueException (String _message) {
        super (_message);
    }

    public CLIOptionArgumentValueException (Throwable _cause) {
        super (_cause);
    }

    public CLIOptionArgumentValueException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public CLIOptionArgumentValueException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
