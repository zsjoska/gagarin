package ro.gagarin.dao;

import ro.gagarin.exceptions.DataConstraintException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.genericrecord.GenericRecord;

public interface GenericTableDAO extends BaseDAO {

    void updateRecord(GenericRecord record) throws OperationException, DataConstraintException;

    void deleteRecord(Long id) throws OperationException, DataConstraintException;
}
