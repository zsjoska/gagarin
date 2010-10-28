package ro.gagarin;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.genericrecord.AppGenRecord;
import ro.gagarin.genericrecord.AppGenRecordField;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;
import ro.gagarin.generictable.GenericTableDAO;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.session.Session;
import ro.gagarin.testutil.TUtil;

public class GenericRecordTests {

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();
    private Session session;

    @Before
    public void init() {
	session = TUtil.createTestSession();
    }

    @After
    public void close() {
	FACTORY.releaseSession(session);
    }

    @Test
    public void testGenericRecordSave() throws Exception {

	AppGenRecord record = new AppGenRecord();
	record.setId(2l);
	AppGenRecordField field1 = new AppGenRecordField().setFieldName("name").setFieldValue("me")
		.setUpdateTimestamp(System.currentTimeMillis());
	record.addField(field1);
	GenericRecordField field2 = record.getField("NAME");
	assertEquals(field1, field2);

	GenericTableDAO genericTableDAO = new GenericTableDAO(session, "UsersExtra");
	genericTableDAO.updateRecord(record);
	GenericRecord record2 = genericTableDAO.getRecord(record.getId());
	System.out.println(record2);
    }
}
