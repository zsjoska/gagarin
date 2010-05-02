package ro.gagarin.exceptions;

public class ExceptionBase extends Exception {

    private static final long serialVersionUID = 5402532633664769260L;

    private final ErrorCodes errorCode;
    private final String detail;

    public ExceptionBase(ErrorCodes errorCode) {
	this.errorCode = errorCode;
	this.detail = null;
    }

    public ExceptionBase(ErrorCodes errorCode, Exception e) {
	super(e);
	this.errorCode = errorCode;
	this.detail = e.getMessage();
    }

    public ExceptionBase(ErrorCodes errorCode, String detail) {
	super(detail);
	this.errorCode = errorCode;
	this.detail = detail;
    }

    public ErrorCodes getErrorCode() {
	return errorCode;
    }

    public String getDetail() {
	return detail;
    }
}
