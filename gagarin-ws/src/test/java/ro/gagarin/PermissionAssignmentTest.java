package ro.gagarin;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.ws.Admin;
import ro.gagarin.ws.Authentication;
import ro.gagarin.ws.executor.WSException;
import ro.gagarin.ws.objects.WSControlEntity;
import ro.gagarin.ws.objects.WSOwner;
import ro.gagarin.ws.objects.WSPermOwnerCEAssignment;
import ro.gagarin.ws.objects.WSUserRole;

public class PermissionAssignmentTest {
    private static Authentication authentication = new Authentication();
    private static String username = "_User_" + System.currentTimeMillis();
    private static String groupname = "_Group_" + System.currentTimeMillis();
    private static String session;
    private static Admin adminService = new Admin();

    @BeforeClass
    public static void startup() throws WSException {
	session = authentication.createSession(null, "TEST");
	authentication.login(session, "admin", "password", null);
    }

    @Test
    public void createPermissionAssignment() throws Exception {
	List<ControlEntityCategory> controlEntityCategories = adminService.getControlEntityCategories(session);
	List<WSControlEntity> ceList = adminService.getControlEntityListForCategory(session, controlEntityCategories
		.get(0).name());

	// read the existing assignments
	List<WSPermOwnerCEAssignment> permissionAssignmentsForControlEntity = adminService
		.getPermissionAssignmentsForControlEntity(session, ceList.get(0));
	int initialCount = permissionAssignmentsForControlEntity.size();

	List<WSOwner> owners = adminService.getOwners(session);
	List<WSUserRole> roleList = adminService.getRoleList(session);

	adminService.assignRoleToControlEntity(session, ceList.get(0), roleList.get(0), owners.get(0));

	permissionAssignmentsForControlEntity = adminService.getPermissionAssignmentsForControlEntity(session,
		ceList.get(0));
	int newSize = permissionAssignmentsForControlEntity.size();

	assertTrue("The count should increase", initialCount < newSize);

    }

    @Test
    public void deletePermissionAssignment() throws Exception {
	List<ControlEntityCategory> controlEntityCategories = adminService.getControlEntityCategories(session);
	List<WSControlEntity> ceList = adminService.getControlEntityListForCategory(session, controlEntityCategories
		.get(0).name());

	// read the existing assignments
	List<WSPermOwnerCEAssignment> permissionAssignmentsForControlEntity = adminService
		.getPermissionAssignmentsForControlEntity(session, ceList.get(0));
	int initialCount = permissionAssignmentsForControlEntity.size();
	WSPermOwnerCEAssignment permOwnerAssignment = permissionAssignmentsForControlEntity.get(0);

	adminService.unAssignRoleFromControlEntity(session, ceList.get(0), permOwnerAssignment.getRole(),
		permOwnerAssignment.getOwner());

	permissionAssignmentsForControlEntity = adminService.getPermissionAssignmentsForControlEntity(session,
		ceList.get(0));
	int newCount = permissionAssignmentsForControlEntity.size();

	assertTrue("The count should decrease", initialCount > newCount);
    }
}
