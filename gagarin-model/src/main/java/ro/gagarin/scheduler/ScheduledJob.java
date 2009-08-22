package ro.gagarin.scheduler;

import ro.gagarin.log.AppLog;
import ro.gagarin.session.Session;
import ro.gagarin.user.BaseEntity;

public abstract class ScheduledJob extends BaseEntity {

	protected static final long serialVersionUID = -238225317665931596L;

	private final String name;
	private final long initialWait;
	private final long period;

	/**
	 * The number of time to execute this job. <code>-1</code> means infinite.
	 */
	private int count;

	public ScheduledJob(String name, long initialWait, long period) {
		this.name = name;
		this.initialWait = initialWait;
		this.period = period;
		this.count = -1;
	}
	
	public ScheduledJob(String name, long initialWait, long period, int count) {
		this.name = name;
		this.initialWait = initialWait;
		this.period = period;
		this.count = count;
	}
	

	public ScheduledJob(String name, long initialWait) {
		this.name = name;
		this.initialWait = initialWait;
		this.period = 0;
		this.count = 1;
	}

	abstract public void execute(Session session, AppLog log) throws Exception;
	
	public String getName() {
		return name;
	}

	public long getInitialWait() {
		return initialWait;
	}

	public long getPeriod() {
		return period;
	}

	@Override
	public String toString() {
		return "ScheduledJob [initialWait=" + initialWait + ", name=" + name + ", period=" + period
				+ ", getId()=" + getId() + "]";
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}
}
