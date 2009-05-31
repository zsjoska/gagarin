package ro.gagarin;

public interface BaseDAO {

	void checkCreateDependencies(ConfigurationManager cfgManager);

	void release();
}
