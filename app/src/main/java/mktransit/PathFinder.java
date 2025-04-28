package mktransit;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class PathFinder {
    private Map<String, Station> stationMap;

    public PathFinder(Map<String, Station> stationMap) {
        this.stationMap = stationMap;
    }

    public PathResult findShortestPath(String startId, String endId) {

        // case sensitive
        startId = startId.toUpperCase();
        endId = endId.toUpperCase();

        Map<String, Integer> times = new HashMap<>(); // เก็บเวลาจาก Id ปัจจุบัน -> Id อื่นๆ
        Map<String, String> previous = new HashMap<>(); // เก็บ Id ก่อนหน้า Id ปัจจุบัน PriorityQueue<Node> queue = new
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.time)); //
        // TODO: ยังไม่ได้ทำอะไร
        return null;
    }

    private static class Node {
        String id;
        int time;

        Node(String id, int time) {
            this.id = id;
            this.time = time;
        }
    }
}
