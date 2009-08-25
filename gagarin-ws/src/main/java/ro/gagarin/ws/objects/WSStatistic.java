package ro.gagarin.ws.objects;

import ro.gagarin.utils.Statistic;

public class WSStatistic {

    private String name;
    private long totalDuration;
    private int count;
    private long max;
    private long min;

    public WSStatistic() {
	// default constructor for WS export
    }

    public WSStatistic(Statistic stat) {
	name = stat.getName();
	totalDuration = stat.getTotalDuration();
	count = stat.getCount();
	max = stat.getMax();
	min = stat.getMin();
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public long getTotalDuration() {
	return totalDuration;
    }

    public void setTotalDuration(long totalDuration) {
	this.totalDuration = totalDuration;
    }

    public int getCount() {
	return count;
    }

    public void setCount(int count) {
	this.count = count;
    }

    public long getMax() {
	return max;
    }

    public void setMax(long max) {
	this.max = max;
    }

    public long getMin() {
	return min;
    }

    public void setMin(long min) {
	this.min = min;
    }

    @Override
    public String toString() {
	return "WSStatistic [count=" + count + ", max=" + max + ", min=" + min + ", name=" + name + ", totalDuration="
		+ totalDuration + "]";
    }

}
