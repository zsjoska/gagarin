package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;

public class GetControlEntityListForCategorySQL extends SelectQuery {

    private final ControlEntityCategory categoryEnum;
    private List<ControlEntity> controlEntities;

    public GetControlEntityListForCategorySQL(BaseJdbcDAO dao, ControlEntityCategory categoryEnum) {
	super(dao);
	this.categoryEnum = categoryEnum;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	this.controlEntities = new ArrayList<ControlEntity>();
	while (rs.next()) {
	    BaseControlEntity ce = new BaseControlEntity(categoryEnum);
	    ce.setId(rs.getLong("id"));
	    ce.setName(rs.getString("name"));
	    this.controlEntities.add(ce);
	}
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	// input was already checked; we use an enum already
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
    }

    @Override
    protected String getSQL() {
	return "SELECT id, name FROM " + this.categoryEnum.table();
    }

    public static List<ControlEntity> execute(BaseJdbcDAO dao, ControlEntityCategory categoryEnum)
	    throws OperationException {
	GetControlEntityListForCategorySQL sql = new GetControlEntityListForCategorySQL(dao, categoryEnum);
	sql.execute();
	return sql.getControlEntities();
    }

    private List<ControlEntity> getControlEntities() {
	return this.controlEntities;
    }
}
