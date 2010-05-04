package ro.gagarin.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.jdbc.objects.DBConfig;
import ro.gagarin.utils.FieldValidator;

public class UpdateConfigValueSQL extends UpdateQuery {

    private final DBConfig config;

    public UpdateConfigValueSQL(BaseJdbcDAO dao, DBConfig config) {
	super(dao, ConfigEntry.class);
	this.config = config;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setString(1, config.getConfigValue());
	stmnt.setString(2, config.getConfigName());
    }

    @Override
    protected String getSQL() {
	return "UPDATE Config SET configValue = ? WHERE configName = ?";
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireStringField("configName", config, true);
	FieldValidator.requireStringField("configValue", config, true);
    }

}
