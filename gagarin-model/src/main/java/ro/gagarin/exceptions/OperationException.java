package ro.gagarin.exceptions;

public class OperationException extends ExceptionBase {

    private static final long serialVersionUID = -8797622648415392209L;

    public OperationException(ErrorCodes errorCode, Exception e) {
	super(errorCode, e);
    }

    public OperationException(ErrorCodes errorCode, String detail) {
	super(errorCode, detail);
    }

}
