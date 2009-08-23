package ro.gagarin.utils;

import java.util.HashMap;

public class StatisticsContainer {

    private static HashMap<String, Statistic> statContainer = new HashMap<String, Statistic>();

    public static void add(Statistic statistic) {
	statContainer.put(statistic.getName(), statistic);
    }

}
