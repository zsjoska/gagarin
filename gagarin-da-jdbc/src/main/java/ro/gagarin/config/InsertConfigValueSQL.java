package ro.gagarin.config;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.gagarin.exceptions.FieldRequiredException;
import ro.gagarin.jdbc.BaseJdbcDAO;
import ro.gagarin.jdbc.UpdateQuery;
import ro.gagarin.jdbc.objects.DBConfig;
import ro.gagarin.utils.FieldValidator;

public class InsertConfigValueSQL extends UpdateQuery {

    private final DBConfig config;

    public InsertConfigValueSQL(BaseJdbcDAO dao, DBConfig config) {
	super(dao, ConfigEntry.class);
	this.config = config;
    }

    @Override
    protected void fillParameters(PreparedStatement stmnt) throws SQLException {
	stmnt.setLong(1, config.getId());
	stmnt.setString(2, config.getConfigName());
	stmnt.setString(3, config.getConfigValue());
    }

    @Override
    protected String getSQL() {
	return "INSERT INTO Config (id, configName, configValue) VALUES (?,?,?)";
    }

    @Override
    protected void checkInput() throws FieldRequiredException {
	FieldValidator.requireLongField("id", config);
	FieldValidator.requireStringField("configName", config, true);
	FieldValidator.requireStringField("configValue", config, true);
    }

}
