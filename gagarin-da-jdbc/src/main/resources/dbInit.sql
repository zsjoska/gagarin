--CHECK: Users
SELECT * FROM Users
--CREATE:
CREATE TABLE Users (id bigint, name varchar(100), userName varchar(50), password varchar(50), roleid bigint)
--END

--CHECK: UserRoles
SELECT * FROM UserRoles
--CREATE:
CREATE TABLE UserRoles (id bigint, roleName varchar(50))
--END

--CHECK: UserPermissions
SELECT * FROM UserPermissions
--CREATE:
CREATE TABLE UserPermissions (id bigint, permissionName varchar(50))
--END

--CHECK: PermissionAssignment
SELECT * FROM PermissionAssignment
--CREATE:
CREATE TABLE PermissionAssignment (role_id bigint, perm_id bigint)
--END
