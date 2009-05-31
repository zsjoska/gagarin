package ro.gagarin.jdbc;

import ro.gagarin.exceptions.ExceptionBase;

public class OperationException extends ExceptionBase {

	private static final long serialVersionUID = -8797622648415392209L;

	public OperationException(int error, Exception e) {
		super(error, e);
	}

	public OperationException(int errorCode, String detail) {
		super(errorCode, detail);
	}

}
