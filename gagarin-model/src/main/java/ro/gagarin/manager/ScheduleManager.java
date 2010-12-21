package ro.gagarin.manager;

import java.util.List;

import ro.gagarin.scheduler.JobController;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.scheduler.SchedulerThread;
import ro.gagarin.session.Session;

public interface ScheduleManager extends BaseManager {

    /**
     * Schedules a job for execution.
     * 
     * @param job
     *            the job to be executed
     * @param createSession
     *            flag to indicate if a session to be created for the job. If
     *            this parameter is <code>true</code>, the job controller must
     *            be able to provide session and application logger for the
     *            execution.
     * @return the id of the created job.
     */
    long scheduleJob(ScheduledJob job, boolean createSession);

    /**
     * Schedules a job for execution.
     * 
     * @param job
     *            the job to be executed
     * @param session
     *            the original(current) session. The implementation should
     *            create a similar session for providing to the job.
     * @return the id of the job.
     */
    long scheduleJob(ScheduledJob job, Session session);

    /**
     * Updates the rate of the job
     * 
     * @param id
     *            the id of the job
     * @param rate
     *            the new execution rate
     */
    void updateJobRate(Long id, Long rate);

    /**
     * Triggers the execution of a job.
     * 
     * @param job
     *            the job to be executed as soon as possible.
     */
    void triggerExecution(ScheduledJob job);

    /**
     * Exports the existing scheduled jobs.
     * 
     * @return a list with the current jobs
     */
    List<JobController> exportJobs();

    /**
     * Exports the current worker threads
     * 
     * @return a list with the actual threads of the scheduler.
     */
    List<SchedulerThread> exportThreads();

}
