package ro.gagarin.jdbc.role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.gagarin.BaseControlEntity;
import ro.gagarin.ControlEntity;
import ro.gagarin.ControlEntityCategory;
import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.exceptions.OperationException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.SelectQuery;
import ro.gagarin.utils.FieldValidator;

public class GetControlEntityByIdAndCategorySQL extends SelectQuery {

    private final Long id;
    private final ControlEntityCategory cat;
    private BaseControlEntity ce;

    public GetControlEntityByIdAndCategorySQL(BaseJdbcDAO dao, Long id, ControlEntityCategory cat) {
	super(dao, ControlEntity.class);
	this.id = id;
	this.cat = cat;
    }

    @Override
    protected void useResult(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    this.ce = new BaseControlEntity(this.cat);
	    this.ce.setId(this.id);
	    this.ce.setName(rs.getString("name"));
	} else {
	    this.ce = null;
	}
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongValue(id, "id");
	if (cat == null) {
	    throw new FieldRequiredException("category", Object.class);
	}
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, this.id);
    }

    @Override
    protected String getSQL() {
	return "SELECT name FROM " + this.cat.table() + " WHERE id = ?";
    }

    public static ControlEntity execute(BaseJdbcDAO dao, Long id, ControlEntityCategory cat) throws OperationException {
	GetControlEntityByIdAndCategorySQL sql = new GetControlEntityByIdAndCategorySQL(dao, id, cat);
	sql.execute();
	return sql.ce;
    }

}
