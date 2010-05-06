package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.utils.FieldValidator;

public class FieldValidatorTest {

    @BeforeClass
    public static void startup() {
    }

    @AfterClass
    public static void shutdown() {
    }

    @Test
    public void basicLong() throws Exception {
	ATestUser user = new ATestUser();
	user.setId(1L);
	FieldValidator.requireLongField("id", user);
    }

    @Test
    public void nullLong() throws Exception {
	try {
	    ATestUser user = new ATestUser();
	    FieldValidator.requireLongField("id", user);
	    fail("the id field is required");
	} catch (FieldRequiredException e) {
	    // expected exception
	    assertEquals("exception must indicate the invalid field", "id", e.getFieldName());
	}
    }

    @Test
    public void invalidObject() throws Exception {
	try {
	    FieldValidator.requireLongField("id", "");
	    fail("String doesn't have getId() method");
	} catch (RuntimeException e) {
	    // expected exception
	}
    }

    @Test
    public void invalidType() throws Exception {
	try {
	    ATestUser user = new ATestUser();
	    user.setUsername("username");
	    FieldValidator.requireLongField("username", user);
	    fail("Username is a string and we required a long value");
	} catch (RuntimeException e) {
	    // expected exception
	}
    }

    @Test
    public void basicString() throws Exception {
	ATestUser user = new ATestUser();
	user.setUsername("1");
	FieldValidator.requireStringField("username", user, true);
    }

    @Test
    public void nullString() throws Exception {
	try {
	    ATestUser user = new ATestUser();
	    FieldValidator.requireStringField("username", user, true);
	    fail("the username field is required");
	} catch (FieldRequiredException e) {
	    // expected exception
	    assertEquals("exception must indicate the invalid field", "username", e.getFieldName());
	}
    }

    @Test
    public void notrimString() throws Exception {
	ATestUser user = new ATestUser();
	user.setUsername(" ");
	FieldValidator.requireStringField("username", user, false);
    }

    @Test
    public void trimString() throws Exception {
	ATestUser user = new ATestUser();
	user.setUsername("a ");
	FieldValidator.requireStringField("username", user, true);
	assertEquals("a", user.getUsername());
    }

    @Test
    public void trimEmptyString() throws Exception {
	try {
	    ATestUser user = new ATestUser();
	    user.setUsername(" ");
	    FieldValidator.requireStringField("username", user, true);
	    fail("the username field is required");
	} catch (FieldRequiredException e) {
	    // expected exception
	    assertEquals("exception must indicate the invalid field", "username", e.getFieldName());
	}
    }
}
