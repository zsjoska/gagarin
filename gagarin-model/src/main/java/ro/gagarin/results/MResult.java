package ro.gagarin.results;

public class MResult {

	private boolean status = false;

	private int statusCode = 0;

	public MResult(boolean status) {
		this.status = status;
	}

	public boolean suceeded() {
		return status;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
