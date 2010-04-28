package ro.gagarin.ws;

import ro.gagarin.exceptions.ExceptionBase;

public class WSException extends Exception {

    private int errorCode;
    private String detail;

    public WSException(ExceptionBase e) {
	super(e);
	errorCode = e.getErrorCode();
	detail = e.getDetail();
    }

    public int getErrorCode() {
	return errorCode;
    }

    public String getDetail() {
	return detail;
    }

}
