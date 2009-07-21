package ro.gagarin;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.session.Session;

public class SchedulerTest {

	private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

	@Test
	public void testSingleExecution() throws Exception {
		ScheduleManager scheduleManager = FACTORY.getScheduleManager();
		final ArrayList<Long> xTimes = new ArrayList<Long>();
		scheduleManager.scheduleJob(new ScheduledJob("testSingleExecution", 10) {
			@Override
			public void execute(Session session) {
				xTimes.add(System.currentTimeMillis());
			}
		});
		Thread.sleep(100);
		assertEquals("Only one time execution was expected", 1, xTimes.size());
	}

	@Test
	public void testMultipleExecution() throws Exception {
		ScheduleManager scheduleManager = FACTORY.getScheduleManager();
		final ArrayList<Long> xTimes = new ArrayList<Long>();
		long start = System.currentTimeMillis();
		scheduleManager.scheduleJob(new ScheduledJob("testMultipleExecution", 10, 35) {
			@Override
			public void execute(Session session) {
				xTimes.add(System.currentTimeMillis());
			}
		});
		Thread.sleep(100);
		for (Long l : xTimes) {
			System.out.println(l - start);
		}
		assertEquals("Only 2 times of execution was expected", 3, xTimes.size());
	}

	@Test
	public void testExceptionExecution() throws Exception {
		ScheduleManager scheduleManager = FACTORY.getScheduleManager();
		scheduleManager.scheduleJob(new ScheduledJob("testExceptionExecution", 10) {
			@Override
			public void execute(Session session) {
				throw new RuntimeException("Test Exception");
			}
		});
	}
}