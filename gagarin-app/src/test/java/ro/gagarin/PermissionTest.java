package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.dao.RoleDAO;
import ro.gagarin.dao.UserDAO;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.testobjects.ATestGroup;
import ro.gagarin.testutil.TUtil;
import ro.gagarin.user.Group;
import ro.gagarin.user.PermissionEnum;
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
    public void assignRoleToOwner() throws Exception {
	UserRole role = TUtil.getAdminRole();
	ATestGroup group = new ATestGroup();
	group.setId(10L);
	roleDAO.assignRoleToOwner(role, group, BaseControlEntity.getAdminEntity());
	Set<UserPermission> perms = roleDAO.getEffectivePermissionsOnEntity(BaseControlEntity.getAdminEntity(), group);
	Map<ControlEntity, Set<UserPermission>> allPerms = roleDAO.getEffectivePermissions(group);
	Set<UserPermission> set = allPerms.get(BaseControlEntity.getAdminEntity());
	assertEquals("the two permission sets must have the same number of elements", perms.size(), set.size());
    }

    @Test
    public void assignToItself() throws Exception {
	UserRole role = TUtil.getAdminRole();
	ATestGroup group = new ATestGroup();
	group.setId(10L);
	roleDAO.assignRoleToOwner(role, group, group);
    }

    @Test
    public void testAdminGroupCEInitialization() throws Exception {
	Group adminGroup = TUtil.getAdminGroup(session);
	Set<UserPermission> perms = roleDAO.getEffectivePermissionsOnEntity(BaseControlEntity.getAdminEntity(),
		adminGroup);
	assertTrue("We need all permissions to be assigned", PermissionEnum.values().length <= perms.size());
    }
}