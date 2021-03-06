-- This file describes the structure of the database required for the application
-- If a database change is required, please place here a tests wheter the database satisfies the new
-- requirement and an update script to bring the database to the required format

-- The file structure is as follows:
--
-- a line starting with -- is considered a comment line and is ignored except
-- the marks detailed below:
-- --CHECK: <req. name> marks the start of a database requirement, this mark should be followed
-- 			by a query script which should fail if the requirement does not exists
--			The <req. name> is only for logging purposes
-- --CREATE: marks the end of the check script and the beginning of the update script which will create, 
-- 			 alter the tables in order to meet the application requirement
-- --END marks the end of a requirement
-- 		 Content after the --END mark is not allowed (lines starting with -- or empty lines are 
--		 permitted)
--
-- SQL statements may span to multiple lines

--CHECK: Users
SELECT * FROM Users
--CREATE:
CREATE TABLE Users 
(
	id bigint, 
	name varchar(100), 
	userName varchar(50) NOT NULL, 
	email varchar(50), 
	phone varchar(50), 
	password varchar(50), 
	roleid bigint NOT NULL,
	
	CONSTRAINT PK_USERS_id PRIMARY KEY (id),
	CONSTRAINT UK_USERS_userName UNIQUE (userName)
)
--END

--CHECK: UserRoles
SELECT * FROM UserRoles
--CREATE:
CREATE TABLE UserRoles 
(
	id bigint, 
	roleName varchar(50) NOT NULL,

	CONSTRAINT PK_ROLES_id PRIMARY KEY (id),
	CONSTRAINT UK_ROLES_roleName UNIQUE (roleName)
)
--END

--CHECK: UserPermissions
SELECT * FROM UserPermissions
--CREATE:
CREATE TABLE UserPermissions 
(
	id bigint, 
	permissionName varchar(50) NOT NULL,

	CONSTRAINT PK_PERMISSIONS_id PRIMARY KEY (id),
	CONSTRAINT UK_PERMISSIONS_permissionName UNIQUE (permissionName)
)
--END

--CHECK: PermissionAssignment
SELECT * FROM PermissionAssignment
--CREATE:
CREATE TABLE PermissionAssignment 
(
	role_id bigint  NOT NULL, 
	perm_id bigint  NOT NULL
)
--END

--CHECK: Config
SELECT * FROM Config
--CREATE:
CREATE TABLE Config 
(
	id bigint, 
	configName varchar(50) NOT NULL,
	configValue varchar(50),

	CONSTRAINT PK_CONFIG_id PRIMARY KEY (id),
	CONSTRAINT UK_CONFIG_configName UNIQUE (configName)
)
--END
