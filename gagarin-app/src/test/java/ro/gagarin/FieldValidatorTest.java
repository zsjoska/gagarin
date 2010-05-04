package ro.gagarin;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.gagarin.testobjects.ATestUser;
import ro.gagarin.utils.FieldValidator;

public class FieldValidatorTest {

    private static final transient Logger LOG = Logger.getLogger(FieldValidatorTest.class);

    @BeforeClass
    public static void startup() {
    }

    @AfterClass
    public static void shutdown() {
    }

    @Test
    public void test() throws Exception {
	ATestUser user = new ATestUser();
	user.setId(1L);
	FieldValidator.requireLongField("id", user);
    }

}
