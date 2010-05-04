package ro.gagarin;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import ro.gagarin.log.AppLog;
import ro.gagarin.scheduler.JobController;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.scheduler.Scheduler;
import ro.gagarin.session.Session;

public class SchedulerTest {

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    @Test
    public void testSingleRunScheduler() throws Exception {

	final ArrayList<Long> xTimes = new ArrayList<Long>();

	ScheduledJob job = new ScheduledJob("testSingleRunScheduler", 10) {
	    public void execute(Session session, AppLog log, JobController jc) throws Exception {
		xTimes.add(System.currentTimeMillis());
	    }
	};

	Scheduler scheduler = new Scheduler();
	scheduler.start();

	long schedTime = System.currentTimeMillis();
	scheduler.scheduleJob(job, false);

	Thread.sleep(200);

	assertEquals("We expect one run only", 1, xTimes.size());
	assertEquals("The expected run time is wrong", schedTime + 10, xTimes.get(0), 50);

    }

    @Test
    public void testMultiRunScheduler() throws Exception {

	final ArrayList<Long> xTimes = new ArrayList<Long>();
	final int count = 5;
	final long rate = 30;

	ScheduledJob job = new ScheduledJob("testMultiRunScheduler", 0, rate, count) {
	    public void execute(Session session, AppLog log, JobController jc) throws Exception {
		xTimes.add(System.currentTimeMillis());
	    }
	};

	Scheduler scheduler = new Scheduler();
	scheduler.start();

	long schedTime = System.currentTimeMillis();
	scheduler.scheduleJob(job, false);

	Thread.sleep(500);

	assertEquals("Too many executions", count, xTimes.size());

	long sum = 0;
	long oldXtime = schedTime - rate;
	for (Long x : xTimes) {
	    assertEquals("Too big difference have passed between the two runs", rate, x - oldXtime, 30);
	    oldXtime = x;
	    sum += x - schedTime;
	}
	assertEquals("The sum of deltas does not integrates in the expected interval", rate * count * (count - 1) / 2,
		sum, 100 * count);

    }

    @Test
    public void testSingleExecutionManager() throws Exception {
	ScheduleManager scheduleManager = FACTORY.getScheduleManager();
	final ArrayList<Long> xTimes = new ArrayList<Long>();
	scheduleManager.scheduleJob(new ScheduledJob("testSingleExecutionManager", 10) {
	    @Override
	    public void execute(Session session, AppLog log, JobController jc) {
		xTimes.add(System.currentTimeMillis());
	    }
	}, false);
	Thread.sleep(100);
	assertEquals("Only one time execution was expected", 1, xTimes.size());
    }

    @Test
    public void testMultipleExecutionManager() throws Exception {
	ScheduleManager scheduleManager = FACTORY.getScheduleManager();
	final ArrayList<Long> xTimes = new ArrayList<Long>();
	long start = System.currentTimeMillis();
	scheduleManager.scheduleJob(new ScheduledJob("testMultipleExecutionManager", 10, 55) {
	    @Override
	    public void execute(Session session, AppLog log, JobController jc) {
		xTimes.add(System.currentTimeMillis());
	    }
	}, false);
	Thread.sleep(100);
	assertEquals("Only 2 times of execution was expected", 2, xTimes.size());
    }

    @Test
    public void testExceptionExecution() throws Exception {
	ScheduleManager scheduleManager = FACTORY.getScheduleManager();
	scheduleManager.scheduleJob(new ScheduledJob("testExceptionExecution", 10) {
	    @Override
	    public void execute(Session session, AppLog log, JobController jc) {
		throw new RuntimeException("Test Exception");
	    }
	}, false);
    }
}
