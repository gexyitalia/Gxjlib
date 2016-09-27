package com.gexy.cli.exception;

public class CLIOptionArgumentRequiredException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public CLIOptionArgumentRequiredException () {

    }

    public CLIOptionArgumentRequiredException (String _message) {
        super (_message);
    }

    public CLIOptionArgumentRequiredException (Throwable _cause) {
        super (_cause);
    }

    public CLIOptionArgumentRequiredException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public CLIOptionArgumentRequiredException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
