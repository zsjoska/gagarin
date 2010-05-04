package ro.gagarin.utils;

public class Statistic {

    private final String name;
    private long totalDuration = 0;
    private int count = 0;
    private long min = Long.MAX_VALUE;
    private long max = 0;

    private Statistic(String name) {
	this.name = name;
	StatisticsContainer.add(this);
    }

    public Statistic(Statistic stat) {
	this.name = stat.name;
	this.totalDuration = stat.totalDuration;
	this.count = stat.count;
	this.min = stat.min;
	this.max = stat.max;
	// and don't add it to the container
    }

    public void addDuration(long duration) {
	synchronized (this) {
	    totalDuration += duration;
	    count++;
	    if (min > duration)
		min = duration;
	    if (max < duration)
		max = duration;
	}
    }

    public String getName() {
	return name;
    }

    public long getTotalDuration() {
	return totalDuration;
    }

    public int getCount() {
	return count;
    }

    public long getMin() {
	return min;
    }

    public long getMax() {
	return max;
    }

    public long getAverage() {
	if (count > 0)
	    return totalDuration / count;
	else
	    return -1;
    }

    public static Statistic getByName(String name) {
	// not synchronized... well I know, not safe but I hope that the loss is
	// less; only the first statistics are lost
	Statistic stat = StatisticsContainer.getByName(name);
	if (stat == null) {
	    stat = new Statistic(name);
	    StatisticsContainer.add(stat);
	}
	return stat;
    }

    public void add(long start) {
	addDuration(System.currentTimeMillis() - start);
    }
}
