package ro.gagarin.manager;

import java.util.List;

import ro.gagarin.scheduler.JobController;
import ro.gagarin.scheduler.ScheduledJob;
import ro.gagarin.scheduler.SchedulerThread;

public interface ScheduleManager extends BaseManager {

    long scheduleJob(ScheduledJob job, boolean createSession);

    void updateJobRate(Long id, Long rate);

    void triggerExecution(ScheduledJob configImportJob);

    List<JobController> exportJobs();

    List<SchedulerThread> exportThreads();

}
