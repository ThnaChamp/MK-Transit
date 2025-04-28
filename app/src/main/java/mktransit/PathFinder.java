package mktransit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
        Map<String, String> previous = new HashMap<>(); // เก็บ Id ก่อนหน้า Id ปัจจุบัน
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.time)); // จัดลำดับตามเวลาที่ใช้
                                                                                                     // (น้อยสุดก่อน)
        // ไล่ทุก Node
        for (String id : stationMap.keySet()) {
            times.put(id, Integer.MAX_VALUE); // ใส่เวลาของ Id ปัจจุบัน -> Id ถัดไป เป็น Infinity
            previous.put(id, null); // ยังไม่มีสถานีก่อนหน้า
        }
        times.put(startId, 0); // ให้ node เริ่มใช้เวลา 0 เพราะว่าเป็นจุดเริ่ม
        queue.add(new Node(startId, 0)); // ใส่ Id เริ่มต้นเข้าใน queue และเวลา เป็น 0

        // Dijkstra
        while (!queue.isEmpty()) { // ทำจนกว่าใน queue จะหมด
            Node current = queue.poll(); // เอาสถานีที่น้อยที่สุด ออกจาก queue Ex. Start = N24 -> current = N24
            Station currentStation = stationMap.get(current.id); // เก็บ Value จาก Key, Value คือ ทั้ง Object

            // ถ้าเจอจุดปลายทางให้หยุด
            if (current.id.equals(endId)) {
                break;
            }

            for (Connection conn : currentStation.getConnections()) {
                Station neighbor = stationMap.get(conn.getTo()); // neighbor เก็บ Id ที่ไปได้
                if (neighbor == null) // ถ้าไม่เจอสถานี (สุดทาง) ข้าม Connection นี้ไป
                    continue;

                int newTime = times.get(current.id) + conn.getTime(); // เวลาก่อนหน้า + เวลาจากจุด ปัจจุบัน -> จุดถัดไป

                // เวลา 0 + เวลาที่ไปอีก Node < เวลาทีถูกกำหนดให้เป็น infinity
                if (newTime < times.get(neighbor.getId())) {
                    times.put(neighbor.getId(), newTime); // ใส่เวลาใหม่เข้าไป
                    previous.put(neighbor.getId(), current.id); // Ex. N24 -> N23 (N23 , N24)
                    queue.add(new Node(neighbor.getId(), newTime)); //ให้ Id ถัดไปเข้ามาอยู่ใน queue และใส่เวลาที่รวมจากเส้นทางก่อนเข้าไป
                }
            }
        }

        // ยังไม่เข้าใจ
        List<String> path = new ArrayList<>();
        String current = endId;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);

        if (path.size() == 1 && !path.get(0).equals(startId)) {
            return new PathResult(new ArrayList<>(), new ArrayList<>(), -1);
        }

        int totalTime = times.get(endId);

        return new PathResult(path, new ArrayList<>(), totalTime);
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
