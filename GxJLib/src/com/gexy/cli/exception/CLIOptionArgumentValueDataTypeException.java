package com.gexy.cli.exception;

public class CLIOptionArgumentValueDataTypeException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public CLIOptionArgumentValueDataTypeException () {

    }

    public CLIOptionArgumentValueDataTypeException (String _message) {
        super (_message);
    }

    public CLIOptionArgumentValueDataTypeException (Throwable _cause) {
        super (_cause);
    }

    public CLIOptionArgumentValueDataTypeException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public CLIOptionArgumentValueDataTypeException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
