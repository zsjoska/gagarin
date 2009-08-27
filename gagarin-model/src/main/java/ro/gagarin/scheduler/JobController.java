package ro.gagarin.scheduler;

public interface JobController {

    long getNextRun();

    void setPeriod(long period);

    long getPeriod();

    void markToExecuteNow();

    void markDone();

}