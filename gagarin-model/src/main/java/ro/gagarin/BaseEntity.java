package ro.gagarin;

public class BaseEntity implements Entity {

    private static long nextId = System.currentTimeMillis();

    private Long id = null; // BaseEntity.getNextId();

    public BaseEntity setId(Long id) {
	this.id = id;
	return this;
    }

    public Long getId() {
	return id;
    }

    public static synchronized long getNextId() {
	return ++nextId;
    }
}
