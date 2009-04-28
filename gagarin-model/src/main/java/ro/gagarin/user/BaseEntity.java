package ro.gagarin.user;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseEntity implements Serializable {

	private static long nextId = System.currentTimeMillis();

	private long id = BaseEntity.getNextId();

	private boolean fromDB = false;

	public void setFromDB(boolean fromDB) {
		this.fromDB = fromDB;
	}

	public boolean isFromDB() {
		return fromDB;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	private static synchronized long getNextId() {
		return ++nextId;
	}
}
