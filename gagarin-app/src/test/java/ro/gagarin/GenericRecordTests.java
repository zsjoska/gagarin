package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.gagarin.genericrecord.AppGenRecord;
import ro.gagarin.genericrecord.AppGenRecordField;
import ro.gagarin.genericrecord.GenericRecord;
import ro.gagarin.genericrecord.GenericRecordField;
import ro.gagarin.generictable.JdbcGenericTableDAO;
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
    public void testGenericRecordLight() throws Exception {

	AppGenRecord record = new AppGenRecord();
	record.setId(2l);
	AppGenRecordField field1 = new AppGenRecordField().setFieldName("name").setFieldValue("me");
	record.addField(field1);
	GenericRecordField field2 = record.getField("NAME");
	assertEquals(field1, field2);

    }

    @Test
    public void testGenericRecordCreate() throws Exception {
	AppGenRecord record = new AppGenRecord();
	record.setId(AppGenRecord.getNextId());
	AppGenRecordField field1 = new AppGenRecordField().setFieldName("name").setFieldValue("me");
	record.addField(field1);

	JdbcGenericTableDAO genericTableDAO = new JdbcGenericTableDAO(session, "UsersExtra");
	genericTableDAO.updateRecord(record);
	GenericRecord record2 = genericTableDAO.getRecord(record.getId());

	assertEquals(record.getId(), record2.getId());
	assertEquals(record.getField("name").getFieldValue(), record2.getField("name").getFieldValue());
    }

    @Test
    public void testGenericRecordUpdate() throws Exception {

	JdbcGenericTableDAO genericTableDAO = new JdbcGenericTableDAO(session, "UsersExtra");

	AppGenRecord record = new AppGenRecord();
	long recordId = AppGenRecord.getNextId();
	record.setId(recordId);
	AppGenRecordField field1 = new AppGenRecordField().setFieldName("name").setFieldValue("me");
	record.addField(field1);

	// create the record and verify
	genericTableDAO.updateRecord(record);
	GenericRecord record2 = genericTableDAO.getRecord(record.getId());

	assertEquals(record.getId(), record2.getId());
	assertEquals(record.getField("name").getFieldValue(), record2.getField("name").getFieldValue());

	// update an existing field and verify
	AppGenRecord update1 = new AppGenRecord(recordId);
	update1.addField(new AppGenRecordField().setFieldName("name").setFieldValue("you"));
	genericTableDAO.updateRecord(update1);
	record2 = genericTableDAO.getRecord(recordId);
	assertEquals("you", record2.getField("name").getFieldValue());

	// update a new field and verify
	AppGenRecord update2 = new AppGenRecord(recordId);
	update2.addField(new AppGenRecordField().setFieldName("address").setFieldValue("there"));
	genericTableDAO.updateRecord(update2);
	record2 = genericTableDAO.getRecord(recordId);
	assertEquals("you", record2.getField("name").getFieldValue());
	assertEquals("there", record2.getField("address").getFieldValue());

	// update to delete a field and verify
	AppGenRecord update3 = new AppGenRecord(recordId);
	update3.addField(new AppGenRecordField().setFieldName("address"));
	genericTableDAO.updateRecord(update3);
	record2 = genericTableDAO.getRecord(recordId);
	assertEquals("you", record2.getField("name").getFieldValue());
	assertEquals(null, record2.getField("address"));

	// try to delete an inexistent field and verify
	AppGenRecord update4 = new AppGenRecord(recordId);
	update4.addField(new AppGenRecordField().setFieldName("address"));
	genericTableDAO.updateRecord(update4);
	record2 = genericTableDAO.getRecord(recordId);
	assertEquals("you", record2.getField("name").getFieldValue());
	assertEquals(null, record2.getField("address"));
    }

    @Test
    public void testGenericRecordDelete() throws Exception {
	AppGenRecord record = new AppGenRecord();
	record.setId(AppGenRecord.getNextId());
	AppGenRecordField field1 = new AppGenRecordField().setFieldName("name").setFieldValue("me");
	record.addField(field1);

	JdbcGenericTableDAO genericTableDAO = new JdbcGenericTableDAO(session, "UsersExtra");
	genericTableDAO.updateRecord(record);
	GenericRecord record2 = genericTableDAO.getRecord(record.getId());

	assertEquals(record.getId(), record2.getId());
	assertEquals(record.getField("name").getFieldValue(), record2.getField("name").getFieldValue());

	genericTableDAO.deleteRecord(record.getId());
	record2 = genericTableDAO.getRecord(record.getId());

	assertNull(record2);
    }
}
