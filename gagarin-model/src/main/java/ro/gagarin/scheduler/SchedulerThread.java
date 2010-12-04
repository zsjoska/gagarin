package ro.gagarin.scheduler;

public interface SchedulerThread {

    String getName();

    GenericJob getActiveJob();
}
