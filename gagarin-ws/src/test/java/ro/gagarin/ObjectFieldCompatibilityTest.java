package ro.gagarin;

import org.junit.Test;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.utils.FieldValidator;
import ro.gagarin.ws.objects.WSConfig;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSEffectivePermission;
import ro.gagarin.ws.objects.WSExportedSession;
import ro.gagarin.ws.objects.WSGroup;
import ro.gagarin.ws.objects.WSLogEntry;
import ro.gagarin.ws.objects.WSOwner;
import ro.gagarin.ws.objects.WSStatistic;
import ro.gagarin.ws.objects.WSUser;
import ro.gagarin.ws.objects.WSUserPermission;
import ro.gagarin.ws.objects.WSUserRole;

public class ObjectFieldCompatibilityTest {

    @Test
    public void wsUser() throws FieldRequiredException {
	WSUser user = new WSUser();
	user.setName("a");
	user.setUsername("a");
	user.setEmail("a");
	user.setPhone("a");
	user.setPassword("a");
	user.setAuthentication("INTERNAL");
	user.setCreated(System.currentTimeMillis());
	FieldValidator.checkAllFields(user);
    }

    @Test
    public void wsConfig() throws FieldRequiredException {
	WSConfig config = new WSConfig();
	config.setConfigName("a");
	config.setConfigValue("a");
	FieldValidator.checkAllFields(config);
    }

    @Test
    public void wsLogEntry() throws FieldRequiredException {
	WSLogEntry le = new WSLogEntry();
	le.setDate(1L);
	le.setLogLevel("a");
	le.setMessage("a");
	le.setSessionID("a");
	FieldValidator.checkAllFields(le);
    }

    @Test
    public void wsUserPermission() throws FieldRequiredException {
	WSUserPermission up = new WSUserPermission();
	up.setPermissionName("a");
	FieldValidator.checkAllFields(up);
    }

    @Test
    public void wsUserRole() throws FieldRequiredException {
	WSUserRole ur = new WSUserRole();
	ur.setRoleName("a");
	FieldValidator.checkAllFields(ur);
    }

    @Test
    public void wsGroup() throws FieldRequiredException {
	WSGroup ug = new WSGroup();
	ug.setDescription("a");
	ug.setName("a");
	FieldValidator.checkAllFields(ug);
    }

    @Test
    public void wsControlEntity() throws FieldRequiredException {
	WSControlEntity ce = new WSControlEntity();
	ce.setName("a");
	FieldValidator.checkAllFields(ce);
    }

    @Test
    public void wsEffectivePermission() throws FieldRequiredException {
	WSEffectivePermission ep = new WSEffectivePermission();
	ep.setName("a");
	FieldValidator.checkAllFields(ep);
    }

    @Test
    public void wsExportedSession() throws FieldRequiredException {
	WSExportedSession es = new WSExportedSession();
	es.setExpires(1L);
	es.setReason("a");
	es.setSessionid("a");
	es.setUsername("a");
	FieldValidator.checkAllFields(es);
    }

    @Test
    public void wsOwner() throws FieldRequiredException {
	WSOwner p = new WSOwner();
	p.setTitle("a");
	FieldValidator.checkAllFields(p);
    }

    @Test
    public void wsStatistic() throws FieldRequiredException {
	WSStatistic s = new WSStatistic();
	s.setName("a");
	FieldValidator.checkAllFields(s);
    }
}
