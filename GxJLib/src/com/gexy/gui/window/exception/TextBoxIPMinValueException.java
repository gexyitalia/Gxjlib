package com.gexy.gui.window.exception;

public class TextBoxIPMinValueException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public TextBoxIPMinValueException () {

    }

    public TextBoxIPMinValueException (String _message) {
        super (_message);
    }

    public TextBoxIPMinValueException (Throwable _cause) {
        super (_cause);
    }

    public TextBoxIPMinValueException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public TextBoxIPMinValueException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
