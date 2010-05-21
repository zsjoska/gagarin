package ro.gagarin;

import java.io.Serializable;

// TODO: remove serializable
@SuppressWarnings("serial")
public class BaseEntity implements Serializable {

    private static long nextId = System.currentTimeMillis();

    private Long id = null; // BaseEntity.getNextId();

    public void setId(Long id) {
	this.id = id;
    }

    public Long getId() {
	return id;
    }

    public static synchronized long getNextId() {
	return ++nextId;
    }
}
