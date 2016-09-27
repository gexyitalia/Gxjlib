package com.gexy.cli.exception;

public class CLIOptionArgumentValueRequiredException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public CLIOptionArgumentValueRequiredException () {

    }

    public CLIOptionArgumentValueRequiredException (String _message) {
        super (_message);
    }

    public CLIOptionArgumentValueRequiredException (Throwable _cause) {
        super (_cause);
    }

    public CLIOptionArgumentValueRequiredException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public CLIOptionArgumentValueRequiredException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
