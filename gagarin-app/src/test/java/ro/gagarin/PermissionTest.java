package ro.gagarin;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestGroup;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.UserPermission;
import ro.gagarin.user.UserRole;

/**
 * Unit test for simple App.
 */
public class PermissionTest {

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    private Session session = null;

    private RoleDAO roleDAO;

    private String groupname;

    private UserDAO userDAO;

    @Before
    public void init() throws Exception {
	this.session = TUtil.createTestSession();
	this.groupname = TUtil.generateID("Group");
	roleDAO = FACTORY.getDAOManager().getRoleDAO(session);
	userDAO = FACTORY.getDAOManager().getUserDAO(session);
    }

    @After
    public void close() {
	FACTORY.releaseSession(session);
    }

    @Test
    public void assignRoleToPerson() throws Exception {
	UserRole role = TUtil.getAdminRole();
	ATestGroup group = new ATestGroup();
	group.setId(10L);
	roleDAO.assignRoleToPerson(role, group, ControlEntity.getAdminEntity());
	Set<UserPermission> perms = roleDAO.getEffectivePermissions(ControlEntity.getAdminEntity(), group);
    }
}