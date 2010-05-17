package ro.gagarin;

import org.junit.Test;

import ro.gagarin.application.objects.AppConfig;
import ro.gagarin.application.objects.AppLogEntry;
import ro.gagarin.application.objects.AppUser;
import ro.gagarin.application.objects.AppUserPermission;
import ro.gagarin.application.objects.AppUserRole;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.utils.FieldValidator;

public class ObjectFieldCompatibilityTest {

    @Test
    public void appUser() throws FieldRequiredException {
	AppUser user = new AppUser();
	user.setName("a");
	user.setUsername("a");
	user.setEmail("a");
	user.setPhone("a");
	user.setPassword("a");
	user.setCreated(System.currentTimeMillis());
	FieldValidator.checkAllFields(user);
    }

    @Test
    public void appConfig() throws FieldRequiredException {
	AppConfig config = new AppConfig();
	config.setConfigName("a");
	config.setConfigValue("a");
	FieldValidator.checkAllFields(config);
    }

    @Test
    public void appLogEntry() throws FieldRequiredException {
	AppLogEntry le = new AppLogEntry();
	le.setDate(1L);
	le.setLogLevel("a");
	le.setMessage("a");
	FieldValidator.checkAllFields(le);
    }

    @Test
    public void appUserPermission() throws FieldRequiredException {
	AppUserPermission up = new AppUserPermission();
	up.setPermissionName("a");
	FieldValidator.checkAllFields(up);
    }

    @Test
    public void appUserRole() throws FieldRequiredException {
	AppUserRole ur = new AppUserRole();
	ur.setRoleName("a");
	FieldValidator.checkAllFields(ur);
    }
}
