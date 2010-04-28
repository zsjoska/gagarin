package ro.gagarin.ws.executor;

import ro.gagarin.utils.Statistic;

public abstract class WebserviceOperation {
    public abstract void execute();

    public abstract Statistic getStatistic();
}
