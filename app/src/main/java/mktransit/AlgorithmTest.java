package mktransit;

import java.util.Map;

public class AlgorithmTest {
    public static void main(String[] args) {
        JsonReader reader = new JsonReader();
        reader.loadJsonData();

        Map<String, Station> stationMap = reader.getStationMap();

        PathFinder pathFinder = new PathFinder(stationMap);

        // ลองหาเส้นทางที่ "ไม่ผ่าน interchange"
        String startId = "N6";
        String endId = "E4";

        PathResult result = pathFinder.findShortestPath(startId, endId);

        if (result.getFullPath().isEmpty()) {
            System.out.println("❌ ไม่พบเส้นทางจาก " + startId + " ไปยัง " + endId);
        } else {
            System.out.println("✅ เจอเส้นทาง!");
            System.out.println("เส้นทางเดินทั้งหมด:");

            for (String stationId : result.getFullPath()) {
                Station station = stationMap.get(stationId);
                System.out.println("- " + station.getName() + " (" + station.getId() + ")");
            }

            System.out.println("\n📍 จุดสำคัญ (Important Steps):");
            for (String stationId : result.getImportantSteps()) {
                Station station = stationMap.get(stationId);
                System.out.println("* " + station.getName() + " (" + station.getId() + ")");
            }

            System.out.println("\n🕒 เวลารวมทั้งหมด: " + result.getTotalTime() + " นาที");
        }
    }
}
