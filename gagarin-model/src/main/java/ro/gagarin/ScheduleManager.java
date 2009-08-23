package ro.gagarin;

import ro.gagarin.scheduler.ScheduledJob;

public interface ScheduleManager {

    long scheduleJob(ScheduledJob job);

    void updateJobRate(Long id, Long rate);

    void triggerExecution(ScheduledJob configImportJob);
}
