package ro.gagarin.utils;

public class Statistic {

    private final String name;
    private long totalDuration = 0;
    private int count = 0;
    private long min = Long.MAX_VALUE;
    private long max = 0;

    public Statistic(String name) {
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
}