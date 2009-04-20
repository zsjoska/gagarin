package org.csovessoft.contabil.user;

public class Entity {

	private String id;
	
	private boolean fromDB = false;

	public void setFromDB(boolean fromDB) {
		this.fromDB = fromDB;
	}

	public boolean isFromDB() {
		return fromDB;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
