package ro.gagarin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import ro.gagarin.config.Configuration;
import ro.gagarin.manager.ManagerFactory;
import ro.gagarin.manager.ScheduleManager;
import ro.gagarin.scheduler.JobController;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.scheduler.Scheduler;
import ro.gagarin.scheduler.SchedulerThread;
import ro.gagarin.session.Session;
import ro.gagarin.testutil.TUtil;

public class SchedulerTest {

    private static final ManagerFactory FACTORY = BasicManagerFactory.getInstance();

    @Test
    public void testSingleRunScheduler() throws Exception {

	final ArrayList<Long> xTimes = new ArrayList<Long>();

	ScheduledJob job = new ScheduledJob("testSingleRunScheduler", 10) {
	    public void execute(JobController jc) throws Exception {
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
	    public void execute(JobController jc) throws Exception {
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
	    public void execute(JobController jc) {
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
	    public void execute(JobController jc) {
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
	    public void execute(JobController jc) {
		throw new RuntimeException("Test Exception");
	    }
	}, false);
    }

    @Test
    public void testIncreaseDecreaseThreadCount() throws Exception {
	Integer schedulerThreads = Configuration.SCHEDULER_THREADS;
	int newCount = schedulerThreads + 2;
	Session session = TUtil.createTestSession();
	try {
	    FACTORY.getConfigurationManager().setConfigValue(session, "SCHEDULER_THREADS", "" + (newCount));
	} finally {
	    FACTORY.releaseSession(session);
	}

	final HashSet<String> threadsTouched = new HashSet<String>();
	final LinkedBlockingQueue<String> step = new LinkedBlockingQueue<String>();
	int jobsToExecute = 100;
	int executeCount = 2;
	ScheduleManager scheduleManager = FACTORY.getScheduleManager();
	for (int i = 0; i < jobsToExecute; i++) {
	    scheduleManager.scheduleJob(new ScheduledJob("testIncreasedThreads", 0, 1, executeCount) {
		@Override
		public void execute(JobController jc) {
		    threadsTouched.add(Thread.currentThread().getName());
		    step.offer(Thread.currentThread().getName() + ":" + getName());
		}
	    }, false);
	}
	for (int i = 0; i < jobsToExecute * executeCount; i++) {
	    System.out.println(step.take());
	}
	for (String name : threadsTouched) {
	    System.out.println(name);
	}
	assertEquals(newCount, threadsTouched.size());

	// now to decrease

	newCount = newCount - 2;
	session = TUtil.createTestSession();
	try {
	    FACTORY.getConfigurationManager().setConfigValue(session, "SCHEDULER_THREADS", "" + (newCount));
	} finally {
	    FACTORY.releaseSession(session);
	}

	Thread.sleep(2000);

	threadsTouched.clear();
	step.clear();
	for (int i = 0; i < jobsToExecute; i++) {
	    scheduleManager.scheduleJob(new ScheduledJob("testIncreasedThreads", 0, 1, executeCount) {
		@Override
		public void execute(JobController jc) {
		    threadsTouched.add(Thread.currentThread().getName());
		    step.offer(Thread.currentThread().getName() + ":" + getName());
		}
	    }, false);
	}
	for (int i = 0; i < jobsToExecute * executeCount; i++) {
	    System.out.println(step.take());
	}
	for (String name : threadsTouched) {
	    System.out.println(name);
	}
	assertEquals(newCount, threadsTouched.size());
    }

    @Test
    public void testExportedThreads() throws Exception {
	ScheduleManager scheduleManager = FACTORY.getScheduleManager();
	List<SchedulerThread> exportThreads = scheduleManager.exportThreads();
	assertEquals(Configuration.SCHEDULER_THREADS, exportThreads.size());
	for (SchedulerThread thread : exportThreads) {
	    System.out.println(thread.getName() + " executing task " + thread.getActiveJob());
	}
    }

    @Test
    public void testExportedJobs() throws Exception {
	ScheduleManager scheduleManager = FACTORY.getScheduleManager();
	long jobId = scheduleManager.scheduleJob(new ScheduledJob("aTestJob", 0, 100) {
	    public void execute(JobController jobController) throws Exception {
	    }
	}, false);
	List<JobController> exportedJobs = scheduleManager.exportJobs();
	for (JobController job : exportedJobs) {
	    System.out.println(job.getName());
	    if ("aTestJob".equalsIgnoreCase(job.getName())) {
		assertEquals(100, job.getPeriod());
	    }
	}

	scheduleManager.updateJobRate(jobId, 101L);

	JobController jc = null;

	exportedJobs = scheduleManager.exportJobs();
	for (JobController job : exportedJobs) {
	    System.out.println(job.getName() + " period:" + job.getPeriod());
	    if ("aTestJob".equalsIgnoreCase(job.getName())) {
		jc = job;
		assertEquals(101, job.getPeriod());
	    }
	}

	assertNotNull(jc);
	jc.markDone();
    }

    @Test
    public void testScheduleWithSession() throws Exception {
	final ArrayList<Long> xTimes = new ArrayList<Long>();

	ScheduledJob job = new ScheduledJob("testScheduleWithSession", 10) {
	    public void execute(JobController jc) throws Exception {
		jc.getLogger().info("Say Hi from session " + jc.getSession().getSessionString());
		xTimes.add(System.currentTimeMillis());
	    }
	};

	Session testSession = TUtil.createTestSession();
	Session cloneSession = FACTORY.getSessionManager().cloneSession(testSession);

	Scheduler scheduler = new Scheduler();
	scheduler.start();

	long schedTime = System.currentTimeMillis();
	scheduler.scheduleJob(job, cloneSession);

	Thread.sleep(200);

	assertEquals("We expect one run only", 1, xTimes.size());
	assertEquals("The expected run time is wrong", schedTime + 10, xTimes.get(0), 50);
    }
}
