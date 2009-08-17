package ro.gagarin;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import ro.gagarin.log.AppLog;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.scheduler.Scheduler;
import ro.gagarin.session.Session;

public class SchedulerTest {

	private static final ManagerFactory FACTORY = BasicManagerFactory
			.getInstance();

	@Test
	public void testSingleRunScheduler() throws Exception {

		final ArrayList<Long> xTimes = new ArrayList<Long>();
		
		ScheduledJob job = new ScheduledJob("testSingleRunScheduler", 10) {
			public void execute(Session session, AppLog log) throws Exception {
				xTimes.add(System.currentTimeMillis());
			}
		};

		Scheduler scheduler = new Scheduler();
		scheduler.start();

		long schedTime = System.currentTimeMillis();
		scheduler.scheduleJob(job);

		Thread.sleep(20);

		assertEquals("We expect one run only", 1, xTimes.size());
		assertEquals("The expected run time is wrong", schedTime + 10, xTimes
				.get(0), 10);

	}

	@Test
	public void testMultiRunScheduler() throws Exception {

		final ArrayList<Long> xTimes = new ArrayList<Long>();
		final int count = 10;

		ScheduledJob job = new ScheduledJob("testMultiRunScheduler", 0, 10, count) {
			public void execute(Session session, AppLog log) throws Exception {
				xTimes.add(System.currentTimeMillis());
			}
		};

		Scheduler scheduler = new Scheduler();
		scheduler.start();

		long schedTime = System.currentTimeMillis();
		scheduler.scheduleJob(job);

		Thread.sleep(200);

		assertEquals("Too many executions", count, xTimes.size());

		long sum = 0;
		long oldXtime = schedTime - 10;
		for (Long x : xTimes) {
			assertEquals("Too big difference have passed between the two runs",
					10, x - oldXtime, 5);
			oldXtime = x;
			sum += x - schedTime;
		}
		assertEquals(
				"The sum of deltas does not integrates in the expected interval",
				10 * count * (count - 1) / 2, sum, 5 * count);

	}

	@Test
	public void testSingleExecutionManager() throws Exception {
		ScheduleManager scheduleManager = FACTORY.getScheduleManager();
		final ArrayList<Long> xTimes = new ArrayList<Long>();
		scheduleManager
				.scheduleJob(new ScheduledJob("testSingleExecutionManager", 10) {
					@Override
					public void execute(Session session, AppLog log) {
						xTimes.add(System.currentTimeMillis());
					}
				});
		Thread.sleep(100);
		assertEquals("Only one time execution was expected", 1, xTimes.size());
	}

	@Test
	public void testMultipleExecutionManager() throws Exception {
		ScheduleManager scheduleManager = FACTORY.getScheduleManager();
		final ArrayList<Long> xTimes = new ArrayList<Long>();
		long start = System.currentTimeMillis();
		scheduleManager.scheduleJob(new ScheduledJob("testMultipleExecutionManager",
				10, 35) {
			@Override
			public void execute(Session session, AppLog log) {
				xTimes.add(System.currentTimeMillis());
			}
		});
		Thread.sleep(100);
		for (Long l : xTimes) {
		}
		assertEquals("Only 2 times of execution was expected", 3, xTimes.size());
	}

	@Test
	public void testExceptionExecution() throws Exception {
		ScheduleManager scheduleManager = FACTORY.getScheduleManager();
		scheduleManager.scheduleJob(new ScheduledJob("testExceptionExecution",
				10) {
			@Override
			public void execute(Session session, AppLog log) {
				throw new RuntimeException("Test Exception");
			}
		});
	}
}
