package ro.gagarin;

public enum ControlEntityCategory {

    ADMIN(null), GROUP("Groups");

    private final String table;

    private ControlEntityCategory(String table) {
	this.table = table;
    }

    public String table() {
	return table;
    }
}
