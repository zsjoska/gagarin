package ro.gagarin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class StatisticsContainer {

    private static ConcurrentHashMap<String, Statistic> statContainer = new ConcurrentHashMap<String, Statistic>(
	    new HashMap<String, Statistic>());

    public static void add(Statistic statistic) {
	statContainer.put(statistic.getName(), statistic);
    }

    public static List<Statistic> exportStatistics(String filter) {
	Pattern pattern = null;
	if (filter != null) {
	    pattern = Pattern.compile(filter);
	}
	ArrayList<Statistic> list = new ArrayList<Statistic>();
	for (Statistic stat : statContainer.values()) {
	    if (pattern == null) {
		list.add(new Statistic(stat));
	    } else if (pattern.matcher(stat.getName()).matches()) {
		list.add(new Statistic(stat));
	    }
	}
	return list;
    }

    public static Statistic getByName(String name) {
	// not synchronized... well I know, not safe but I hope that the loss is
	// less; only the first statistics are lost
	Statistic statistic = statContainer.get(name);
	if (statistic == null) {
	    statistic = new Statistic(name);
	}
	return statistic;
    }
}
