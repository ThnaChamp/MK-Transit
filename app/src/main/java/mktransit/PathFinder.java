package mktransit;

import java.util.Map;

public class PathFinder {
    private Map<String, Station> stationMap;

    public PathFinder(Map<String, Station> stationMap) {
        this.stationMap = stationMap;
    }
}
