package org.csovessoft.contabil.user;

public class Entity {

	private boolean fromDB = false;

	public void setFromDB(boolean fromDB) {
		this.fromDB = fromDB;
	}

	public boolean isFromDB() {
		return fromDB;
	}
}
