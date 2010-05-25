package ro.gagarin.ws.objects;

import java.util.List;

import ro.gagarin.BaseEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.user.PermissionEnum;

public class WSEffectivePermission extends BaseEntity implements ControlEntity {

    private List<PermissionEnum> permissions;
    private ControlEntityCategory cat;
    private String name;

    public WSEffectivePermission() {
    }

    public WSEffectivePermission(ControlEntity ce, List<PermissionEnum> permissions) {
	this.setId(ce.getId());
	this.name = ce.getName();
	this.permissions = permissions;
	this.cat = ce.getCategory();
    }

    public void setPermissions(List<PermissionEnum> permissions) {
	this.permissions = permissions;
    }

    public List<PermissionEnum> getPermissions() {
	return permissions;
    }

    @Override
    public ControlEntityCategory getCategory() {
	return this.cat;
    }

    @Override
    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setCat(ControlEntityCategory cat) {
	this.cat = cat;
    }
}
