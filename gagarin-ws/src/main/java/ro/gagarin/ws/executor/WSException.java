package ro.gagarin.ws.executor;

import ro.gagarin.exceptions.ErrorCodes;
import ro.gagarin.exceptions.ExceptionBase;

public class WSException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 8399343260675574882L;
    private ErrorCodes errorCode;
    private String detail;

    public WSException(ExceptionBase e) {
	super(e);
	errorCode = e.getErrorCode();
	detail = e.getDetail();
    }

    public ErrorCodes getErrorCode() {
	return errorCode;
    }

    public String getDetail() {
	return detail;
    }

}
