package ro.gagarin.jdbc.objects;

import ro.gagarin.Owner;
import ro.gagarin.OwnerTypesEnum;

public class DBOwner implements Owner {

    private OwnerTypesEnum type;
    private String title;
    private Long id;

    public DBOwner(Long id) {
	this.id = id;
    }

    @Override
    public Long getId() {
	return this.id;
    }

    @Override
    public String getTitle() {
	return this.title;
    }

    @Override
    public OwnerTypesEnum getType() {
	return this.type;
    }

}
