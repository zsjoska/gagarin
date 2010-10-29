package ro.gagarin.dao;

import ro.gagarin.exceptions.OperationException;
import ro.gagarin.user.UserExtraRecord;

public interface UserExtraDAO extends GenericTableDAO {
    UserExtraRecord getRecord(long id) throws OperationException;

}
