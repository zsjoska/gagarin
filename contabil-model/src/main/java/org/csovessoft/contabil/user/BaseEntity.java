package org.csovessoft.contabil.user;

import java.io.Serializable;

public class BaseEntity implements Serializable {

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
