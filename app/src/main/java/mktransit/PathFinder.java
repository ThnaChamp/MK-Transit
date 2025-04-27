package mktransit;

import java.util.Map;

public class PathFinder {
    private Map<String, Station> stationMap;

    public PathFinder(Map<String, Station> stationMap) {
        this.stationMap = stationMap;
    }

    public PathResult findShortestPath(String startId, String endId) {
        startId = startId.toUpperCase();
        endId = endId.toUpperCase();

        
        // TODO: ยังไม่ได้ทำอะไร
        return null;
    }
}
