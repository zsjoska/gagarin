package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.utils.ConversionUtils;

public class WSControlEntity extends BaseEntity implements ControlEntity {

    private ControlEntityCategory category;
    private String name;

    public WSControlEntity() {

    }

    public WSControlEntity(ControlEntity controlEntity) {
	this.setId(controlEntity.getId());
	this.category = controlEntity.getCategory();
	this.name = controlEntity.getName();
    }

    @Override
    public ControlEntityCategory getCategory() {
	return this.category;
    }

    @Override
    public String getName() {
	return this.name;
    }

    public void setCategory(ControlEntityCategory category) {
	this.category = category;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return ConversionUtils.controlEntity2String(this);
    }
}
