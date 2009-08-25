package ro.gagarin.exceptions;

public class LoginRequiredException extends ExceptionBase {

    public LoginRequiredException() {
	super(ErrorCodes.LOGIN_REQUIRED);
    }

}
