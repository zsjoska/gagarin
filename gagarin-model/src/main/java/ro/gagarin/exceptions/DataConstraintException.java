package ro.gagarin.exceptions;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataConstraintException extends ExceptionBase {

    private static final long serialVersionUID = -4778696106011385988L;
    private static final Pattern EMPTY_COL = Pattern.compile("Column '(.*)'  cannot accept a NULL value.");
    private static final Pattern DUPL_COL = Pattern
	    .compile("The statement was aborted because it would have caused a duplicate key value in a unique or primary key constraint or unique index identified by '(.*)' defined on '(.*)'.");
    private static final Pattern CONSTRAINT = Pattern.compile(".K_.*_(.*)");

    public DataConstraintException(ErrorCodes errorCode) {
	super(errorCode);
    }

    public DataConstraintException(ErrorCodes errorCode, Exception e) {
	super(errorCode, e);
    }

    public static DataConstraintException createException(SQLException e, Class<?> aClass) {
	Matcher matcher;
	String message = e.getMessage();
	matcher = EMPTY_COL.matcher(message);
	if (matcher.matches()) {
	    return new FieldRequiredException(matcher.group(1), aClass, e);
	}
	matcher = DUPL_COL.matcher(message);
	if (matcher.matches()) {
	    return new ItemExistsException(aClass, constrait2Field(matcher.group(1)), e);
	}

	return new DataConstraintException(ErrorCodes.DB_OP_ERROR, e);

    }

    private static String constrait2Field(String group) {
	if (group == null) {
	    return null;
	}
	Matcher matcher = CONSTRAINT.matcher(group);
	if (matcher.matches()) {
	    return matcher.group(1);
	} else {
	    return group;
	}
    }
}
