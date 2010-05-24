package ro.gagarin.ws.objects;

import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;

public class WSControlEntity implements ControlEntity {
    private Long id;
    private String name;
    private ControlEntityCategory category;

    public WSControlEntity() {
    }

    public WSControlEntity(ControlEntity ce) {
	this.id = ce.getId();
	this.name = ce.getName();
	this.category = ce.getCat();
    }

    @Override
    public ControlEntityCategory getCat() {
	return this.category;
    }

    @Override
    public Long getId() {
	return this.id;
    }

    @Override
    public String getName() {
	return this.name;
    }
}
