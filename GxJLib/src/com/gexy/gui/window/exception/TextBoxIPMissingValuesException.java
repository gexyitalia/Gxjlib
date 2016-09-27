package com.gexy.gui.window.exception;

public class TextBoxIPMissingValuesException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public TextBoxIPMissingValuesException () {

    }

    public TextBoxIPMissingValuesException (String _message) {
        super (_message);
    }

    public TextBoxIPMissingValuesException (Throwable _cause) {
        super (_cause);
    }

    public TextBoxIPMissingValuesException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public TextBoxIPMissingValuesException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
