package ro.gagarin.dummyimpl;

import ro.gagarin.BaseDAO;

public class DummyBase implements BaseDAO {

	@Override
	public void checkCreateDependencies() {
		// nothing required
	}

	@Override
	public void release() {
		// nothing to release
	}

}
