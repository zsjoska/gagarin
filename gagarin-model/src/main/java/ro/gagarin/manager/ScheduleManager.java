package ro.gagarin.manager;

import ro.gagarin.scheduler.ScheduledJob;

public interface ScheduleManager extends BaseManager {

    long scheduleJob(ScheduledJob job, boolean createSession);

    void updateJobRate(Long id, Long rate);

    void triggerExecution(ScheduledJob configImportJob);
}
