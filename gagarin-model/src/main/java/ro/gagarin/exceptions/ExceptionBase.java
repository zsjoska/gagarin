package ro.gagarin.exceptions;

public class ExceptionBase extends Exception {

    private static final long serialVersionUID = 5402532633664769260L;

    private final int errorCode;
    private final String detail;

    public ExceptionBase(int errorCode) {
	this.errorCode = errorCode;
	this.detail = null;
    }

    public ExceptionBase(int errorCode, Exception e) {
	super(e);
	this.errorCode = errorCode;
	this.detail = e.getMessage();
    }

    public ExceptionBase(int errorCode, String detail) {
	super(detail);
	this.errorCode = errorCode;
	this.detail = detail;
    }

    public int getErrorCode() {
	return errorCode;
    }

    public String getDetail() {
	return detail;
    }
}
