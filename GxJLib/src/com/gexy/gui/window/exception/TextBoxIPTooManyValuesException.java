package com.gexy.gui.window.exception;

public class TextBoxIPTooManyValuesException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public TextBoxIPTooManyValuesException () {

    }

    public TextBoxIPTooManyValuesException (String _message) {
        super (_message);
    }

    public TextBoxIPTooManyValuesException (Throwable _cause) {
        super (_cause);
    }

    public TextBoxIPTooManyValuesException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public TextBoxIPTooManyValuesException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
