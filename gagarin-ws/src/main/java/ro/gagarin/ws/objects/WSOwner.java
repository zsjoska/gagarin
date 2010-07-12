package ro.gagarin.ws.objects;

import ro.gagarin.BaseEntity;
import ro.gagarin.Owner;
import ro.gagarin.OwnerTypesEnum;
import ro.gagarin.user.User;
import ro.gagarin.utils.ConversionUtils;

public class WSOwner extends BaseEntity implements Owner {

    private OwnerTypesEnum type;
    private String title;

    public WSOwner() {

    }

    public WSOwner(Owner owner) {
	this.setId(owner.getId());
	this.setTitle(owner.getTitle());
	this.type = owner.getType();
    }

    public WSOwner(User user) {
	this.setId(user.getId());
	this.setTitle(user.getTitle());
	this.type = user.getType();
    }

    @Override
    public OwnerTypesEnum getType() {
	return this.type;
    }

    public void setType(OwnerTypesEnum type) {
	this.type = type;
    }

    @Override
    public String toString() {
	return ConversionUtils.owner2String(this);
    }

    @Override
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }
}
