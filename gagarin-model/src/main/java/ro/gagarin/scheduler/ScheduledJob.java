package ro.gagarin.scheduler;

import ro.gagarin.user.BaseEntity;

public abstract class ScheduledJob extends BaseEntity implements Runnable {

	private static final long serialVersionUID = -238225317665931596L;

	private final String name;
	private final long initialWait;
	private final long period;

	public ScheduledJob(String name, long initialWait, long period) {
		this.name = name;
		this.initialWait = initialWait;
		this.period = period;
	}

	public ScheduledJob(String name, long initialWait) {
		this.name = name;
		this.initialWait = initialWait;
		this.period = 0;
	}

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
}
